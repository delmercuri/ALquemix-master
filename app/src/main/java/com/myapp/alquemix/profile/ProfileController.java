package com.myapp.alquemix.profile;

import com.google.firebase.Timestamp;
import com.myapp.alquemix.auth.Usuario;
import com.myapp.alquemix.auth.UsuarioRepository;
import com.myapp.alquemix.quiz.Intento;
import com.myapp.alquemix.quiz.IntentosRepository;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controlador de Perfil - Módulo de Perfil
 */
public class ProfileController {

    private static ProfileController instance;
    private final UsuarioRepository usuarioRepository;
    private final IntentosRepository intentosRepository;

    private ProfileController() {
        usuarioRepository = UsuarioRepository.getInstance();
        intentosRepository = IntentosRepository.getInstance();
    }

    public static synchronized ProfileController getInstance() {
        if (instance == null) {
            instance = new ProfileController();
        }
        return instance;
    }

    public interface ProfileCallback {
        void onDatosCargados(Usuario usuario, EstadisticasUsuario estadisticas);

        void onError(String mensaje);
    }

    public void cargarPerfil(ProfileCallback callback) {
        String uid = usuarioRepository.getCurrentUid();
        if (uid == null) {
            callback.onError("Usuario no autenticado");
            return;
        }

        usuarioRepository.getUsuario(uid, new UsuarioRepository.Callback<Usuario>() {
            @Override
            public void onSuccess(Usuario usuario) {
                if (usuario == null) {
                    callback.onError("No se encontró el usuario");
                    return;
                }

                intentosRepository.getIntentos(uid, new IntentosRepository.Callback<List<Intento>>() {
                    @Override
                    public void onSuccess(List<Intento> intentos) {
                        EstadisticasUsuario stats = calcularEstadisticas(intentos);
                        callback.onDatosCargados(usuario, stats);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onDatosCargados(usuario, new EstadisticasUsuario());
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                callback.onError("Error al cargar perfil: " + e.getMessage());
            }
        });
    }

    public EstadisticasUsuario calcularEstadisticas(List<Intento> intentos) {
        EstadisticasUsuario stats = new EstadisticasUsuario();

        if (intentos == null || intentos.isEmpty())
            return stats;

        int totalPreguntas = 0, totalCorrectas = 0, mejorPuntaje = 0;
        Set<Integer> temasCompletadosSet = new HashSet<>(); // Temas con >= 60%
        Set<Integer> temasIntentadosSet = new HashSet<>(); // Todos los temas intentados
        Set<String> diasConActividad = new HashSet<>();

        // Track best score per theme
        Map<Integer, Integer> mejorPuntajePorTema = new java.util.HashMap<>();

        for (Intento intento : intentos) {
            totalCorrectas += intento.getCorrectas();
            totalPreguntas += intento.getTotalPreguntas();

            int porcentaje = intento.getPorcentaje();
            int temaId = intento.getTemaId();

            if (temaId > 0) {
                temasIntentadosSet.add(temaId);

                // Track best score per theme
                int mejorAnterior = mejorPuntajePorTema.getOrDefault(temaId, 0);
                if (porcentaje > mejorAnterior) {
                    mejorPuntajePorTema.put(temaId, porcentaje);
                }

                // Only count as "completed" if scored >= 60%
                if (porcentaje >= 60) {
                    temasCompletadosSet.add(temaId);
                }
            }

            if (porcentaje > mejorPuntaje)
                mejorPuntaje = porcentaje;

            Timestamp fecha = intento.getFecha();
            if (fecha != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fecha.toDate());
                diasConActividad.add(cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.DAY_OF_YEAR));
            }
        }

        int porcentajeTotal = totalPreguntas > 0 ? (int) (totalCorrectas * 100.0 / totalPreguntas) : 0;
        int[] rachas = calcularRachas(diasConActividad);

        stats.setTemasCompletados(temasCompletadosSet.size());
        stats.setPorcentajeProgreso(porcentajeTotal);
        stats.setQuizzesCompletados(temasIntentadosSet.size()); // Unique themes attempted, not total attempts
        stats.setPuntajePromedio(porcentajeTotal);
        stats.setMejorPuntaje(mejorPuntaje);
        stats.setRachaActual(rachas[0]);
        stats.setMejorRacha(rachas[1]);

        return stats;
    }

    public int[] calcularRachas(Set<String> diasConActividad) {
        if (diasConActividad == null || diasConActividad.isEmpty())
            return new int[] { 0, 0 };

        int rachaActual = 0, mejorRacha = 0, rachaTemp = 0;
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 365; i++) {
            String diaKey = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.DAY_OF_YEAR);
            if (diasConActividad.contains(diaKey)) {
                rachaTemp++;
                if (i == 0 || rachaActual > 0)
                    rachaActual = rachaTemp;
            } else {
                if (rachaTemp > mejorRacha)
                    mejorRacha = rachaTemp;
                rachaTemp = 0;
                if (i == 0)
                    rachaActual = 0;
            }
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        if (rachaTemp > mejorRacha)
            mejorRacha = rachaTemp;
        if (rachaActual > mejorRacha)
            mejorRacha = rachaActual;

        return new int[] { rachaActual, mejorRacha };
    }
}
