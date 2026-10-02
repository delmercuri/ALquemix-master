package com.myapp.alquemix.quiz;

import android.content.Context;
import android.content.SharedPreferences;

import com.myapp.alquemix.auth.UsuarioRepository;

import java.util.List;

/**
 * Controlador de Quiz - Módulo de Quiz
 */
public class QuizController {

    private final QuizRepository quizRepository;
    private final IntentosRepository intentosRepository;
    private final UsuarioRepository usuarioRepository;

    private List<Pregunta> preguntasActuales;
    private int indiceActual;
    private int aciertos;
    private int temaId;
    private String nombreTema;
    private boolean quizFinalizado;

    private static final int PUNTOS_POR_ACIERTO = 20;

    public QuizController() {
        quizRepository = QuizRepository.getInstance();
        intentosRepository = IntentosRepository.getInstance();
        usuarioRepository = UsuarioRepository.getInstance();
        reset();
    }

    public interface QuizCallback {
        void onPreguntaCargada(Pregunta pregunta, int indice, int total);

        void onRespuestaCorrecta(int aciertos);

        void onRespuestaIncorrecta(int respuestaCorrecta, int aciertos);

        void onQuizFinalizado(int aciertos, int total, int puntosGanados);

        void onError(String mensaje);
    }

    public void iniciarQuiz(int temaId, String nombreTema) {
        this.temaId = temaId;
        this.nombreTema = nombreTema;
        this.indiceActual = 0;
        this.aciertos = 0;
        this.quizFinalizado = false;
        this.preguntasActuales = quizRepository.getPreguntasPorTema(temaId, true);
    }

    public void reset() {
        this.preguntasActuales = null;
        this.indiceActual = 0;
        this.aciertos = 0;
        this.temaId = 0;
        this.nombreTema = null;
        this.quizFinalizado = false;
    }

    public Pregunta getPreguntaActual() {
        if (preguntasActuales == null || indiceActual >= preguntasActuales.size()) {
            return null;
        }
        return preguntasActuales.get(indiceActual);
    }

    public void responder(int respuestaSeleccionada, QuizCallback callback) {
        Pregunta preguntaActual = getPreguntaActual();
        if (preguntaActual == null) {
            callback.onError("No hay pregunta actual");
            return;
        }

        boolean esCorrecta = preguntaActual.esCorrecta(respuestaSeleccionada);

        if (esCorrecta) {
            aciertos++;
            callback.onRespuestaCorrecta(aciertos);
        } else {
            callback.onRespuestaIncorrecta(preguntaActual.getRespuestaCorrecta(), aciertos);
        }
    }

    public void siguientePregunta(QuizCallback callback) {
        indiceActual++;

        if (indiceActual < preguntasActuales.size()) {
            callback.onPreguntaCargada(
                    preguntasActuales.get(indiceActual),
                    indiceActual,
                    preguntasActuales.size());
        } else {
            finalizarQuiz(callback);
        }
    }

    private void finalizarQuiz(QuizCallback callback) {
        if (quizFinalizado)
            return;
        quizFinalizado = true;

        int total = preguntasActuales.size();
        int puntosGanados = aciertos * PUNTOS_POR_ACIERTO;

        String uid = usuarioRepository.getCurrentUid();
        if (uid != null) {
            Intento intento = new Intento(aciertos, total, nombreTema, temaId);
            intentosRepository.guardarIntento(uid, intento, null);
            usuarioRepository.agregarPuntos(uid, puntosGanados);
        }

        callback.onQuizFinalizado(aciertos, total, puntosGanados);
    }

    public void guardarProgresoLocal(Context context) {
        String uid = usuarioRepository.getCurrentUid();
        if (uid == null)
            uid = "guest";

        SharedPreferences prefs = context.getSharedPreferences("AlquemixPrefs", Context.MODE_PRIVATE);
        String userKey = uid + "_";
        prefs.edit()
                .putInt(userKey + "tema_" + temaId + "_aciertos", aciertos)
                .putInt(userKey + "puntos_totales",
                        prefs.getInt(userKey + "puntos_totales", 0) + aciertos * PUNTOS_POR_ACIERTO)
                .apply();
    }

    // Getters
    public int getIndiceActual() {
        return indiceActual;
    }

    public int getAciertos() {
        return aciertos;
    }

    public int getTotalPreguntas() {
        return preguntasActuales != null ? preguntasActuales.size() : 0;
    }

    public int getTemaId() {
        return temaId;
    }

    public String getNombreTema() {
        return nombreTema;
    }

    public boolean isQuizFinalizado() {
        return quizFinalizado;
    }

    public int getPuntosGanados() {
        return aciertos * PUNTOS_POR_ACIERTO;
    }

    public List<Pregunta> getPreguntas() {
        return preguntasActuales;
    }
}
