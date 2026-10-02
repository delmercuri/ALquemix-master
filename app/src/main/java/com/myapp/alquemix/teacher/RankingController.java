package com.myapp.alquemix.teacher;

import com.myapp.alquemix.auth.Usuario;
import com.myapp.alquemix.auth.UsuarioRepository;
import com.myapp.alquemix.quiz.Intento;
import com.myapp.alquemix.quiz.IntentosRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controlador de Ranking - Módulo de Docente
 */
public class RankingController {

    private static RankingController instance;
    private final UsuarioRepository usuarioRepository;
    private final IntentosRepository intentosRepository;

    private RankingController() {
        usuarioRepository = UsuarioRepository.getInstance();
        intentosRepository = IntentosRepository.getInstance();
    }

    public static synchronized RankingController getInstance() {
        if (instance == null) {
            instance = new RankingController();
        }
        return instance;
    }

    public static class EstudianteRanking {
        public String nombre, iniciales;
        public double porcentaje;
        public int temasCompletados, quizzesRealizados;

        public EstudianteRanking(String nombre, String iniciales, double porcentaje,
                int temasCompletados, int quizzesRealizados) {
            this.nombre = nombre;
            this.iniciales = iniciales;
            this.porcentaje = porcentaje;
            this.temasCompletados = temasCompletados;
            this.quizzesRealizados = quizzesRealizados;
        }
    }

    public static class EstadisticasTema {
        public String tema;
        public int promedioGeneral;

        public EstadisticasTema(String tema, int promedio) {
            this.tema = tema;
            this.promedioGeneral = promedio;
        }
    }

    public interface RankingCallback {
        void onRankingCargado(List<EstudianteRanking> ranking);

        void onError(String mensaje);
    }

    public interface EstadisticasDocenteCallback {
        void onEstadisticasCargadas(int totalEstudiantes, int promedioGeneral,
                List<EstudianteRanking> alumnos,
                List<EstadisticasTema> temas);

        void onError(String mensaje);
    }

    public void cargarRanking(RankingCallback callback) {
        usuarioRepository.getEstudiantes(20, new UsuarioRepository.Callback<List<Usuario>>() {
            @Override
            public void onSuccess(List<Usuario> usuarios) {
                if (usuarios.isEmpty()) {
                    callback.onRankingCargado(new ArrayList<>());
                    return;
                }

                final List<EstudianteRanking> ranking = new ArrayList<>();
                final int[] procesados = { 0 };
                final int total = usuarios.size();

                for (Usuario usuario : usuarios) {
                    intentosRepository.getIntentos(usuario.getUid(), new IntentosRepository.Callback<List<Intento>>() {
                        @Override
                        public void onSuccess(List<Intento> intentos) {
                            ranking.add(calcularEstadisticasEstudiante(usuario, intentos));
                            procesados[0]++;
                            if (procesados[0] == total) {
                                Collections.sort(ranking, (a, b) -> Double.compare(b.porcentaje, a.porcentaje));
                                callback.onRankingCargado(ranking);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            ranking.add(new EstudianteRanking(usuario.getNombreCompleto(), usuario.getIniciales(), 0, 0,
                                    0));
                            procesados[0]++;
                            if (procesados[0] == total) {
                                Collections.sort(ranking, (a, b) -> Double.compare(b.porcentaje, a.porcentaje));
                                callback.onRankingCargado(ranking);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onError("Error al cargar estudiantes: " + e.getMessage());
            }
        });
    }

    public void cargarEstadisticasDocente(EstadisticasDocenteCallback callback) {
        usuarioRepository.getEstudiantes(50, new UsuarioRepository.Callback<List<Usuario>>() {
            @Override
            public void onSuccess(List<Usuario> usuarios) {
                if (usuarios.isEmpty()) {
                    callback.onEstadisticasCargadas(0, 0, new ArrayList<>(), new ArrayList<>());
                    return;
                }

                final List<EstudianteRanking> alumnos = new ArrayList<>();
                final List<EstadisticasTema> temas = new ArrayList<>();
                final double[] sumaGeneral = { 0 };
                final int[] procesados = { 0 };
                final int total = usuarios.size();

                for (Usuario usuario : usuarios) {
                    intentosRepository.getIntentos(usuario.getUid(), new IntentosRepository.Callback<List<Intento>>() {
                        @Override
                        public void onSuccess(List<Intento> intentos) {
                            EstudianteRanking estudiante = calcularEstadisticasEstudiante(usuario, intentos);
                            alumnos.add(estudiante);
                            sumaGeneral[0] += estudiante.porcentaje;

                            Set<String> temasVistos = new HashSet<>();
                            for (Intento i : intentos) {
                                String tema = i.getTema();
                                if (tema != null && !temasVistos.contains(tema)) {
                                    temasVistos.add(tema);
                                    temas.add(new EstadisticasTema(tema, i.getPorcentaje()));
                                }
                            }

                            procesados[0]++;
                            if (procesados[0] == total) {
                                int promedioGeneral = (int) (sumaGeneral[0] / total);
                                Collections.sort(temas,
                                        (a, b) -> Integer.compare(b.promedioGeneral, a.promedioGeneral));
                                callback.onEstadisticasCargadas(total, promedioGeneral, alumnos, temas);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            procesados[0]++;
                            if (procesados[0] == total) {
                                int promedioGeneral = alumnos.isEmpty() ? 0 : (int) (sumaGeneral[0] / alumnos.size());
                                callback.onEstadisticasCargadas(total, promedioGeneral, alumnos, temas);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onError("Error al cargar estadísticas: " + e.getMessage());
            }
        });
    }

    private EstudianteRanking calcularEstadisticasEstudiante(Usuario usuario, List<Intento> intentos) {
        int correctas = 0, totalPreguntas = 0;
        Set<String> temasSet = new HashSet<>();

        for (Intento intento : intentos) {
            correctas += intento.getCorrectas();
            totalPreguntas += intento.getTotalPreguntas();
            if (intento.getTema() != null)
                temasSet.add(intento.getTema());
        }

        double porcentaje = totalPreguntas > 0 ? correctas * 100.0 / totalPreguntas : 0;
        return new EstudianteRanking(usuario.getNombreCompleto(), usuario.getIniciales(),
                porcentaje, temasSet.size(), intentos.size());
    }
}
