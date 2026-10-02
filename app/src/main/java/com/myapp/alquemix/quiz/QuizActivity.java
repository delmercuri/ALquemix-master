package com.myapp.alquemix.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.myapp.alquemix.R;
import com.myapp.alquemix.profile.ProfileActivity;

/**
 * Vista de Quiz - Módulo de Quiz
 */
public class QuizActivity extends AppCompatActivity {

    private TextView tvPregunta, tvNumeroPregunta;
    private ImageView ivPregunta;
    private RadioGroup rgOpciones;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button btnSiguiente;
    private CardView cardProfile;
    private ImageButton btnClose;

    private QuizController quizController;
    private boolean esperandoSiguiente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        int temaId = getIntent().getIntExtra("TEMA_ID", 1);
        String nombreTema = getIntent().getStringExtra("TEMA_NOMBRE");

        quizController = new QuizController();
        quizController.iniciarQuiz(temaId, nombreTema);

        initViews();
        mostrarPreguntaActual();
    }

    private void initViews() {
        tvPregunta = findViewById(R.id.tv_pregunta);
        tvNumeroPregunta = findViewById(R.id.tv_numero_pregunta);
        ivPregunta = findViewById(R.id.iv_pregunta);
        rgOpciones = findViewById(R.id.rg_opciones);
        rb1 = findViewById(R.id.rb_opcion1);
        rb2 = findViewById(R.id.rb_opcion2);
        rb3 = findViewById(R.id.rb_opcion3);
        rb4 = findViewById(R.id.rb_opcion4);
        btnSiguiente = findViewById(R.id.btn_siguiente);
        btnSiguiente.setEnabled(false);

        // Header elements
        cardProfile = findViewById(R.id.card_profile);
        btnClose = findViewById(R.id.btn_close);

        // Profile navigation
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Close button
        btnClose.setOnClickListener(v -> finish());

        btnSiguiente.setOnClickListener(v -> {
            if (quizController.isQuizFinalizado()) {
                finish();
            } else {
                avanzarSiguiente();
            }
        });
    }

    private void mostrarPreguntaActual() {
        Pregunta pregunta = quizController.getPreguntaActual();
        if (pregunta == null) {
            finalizarQuiz();
            return;
        }

        int indice = quizController.getIndiceActual();
        int total = quizController.getTotalPreguntas();

        // Update header with progress
        tvNumeroPregunta.setText("Pregunta " + (indice + 1) + "/" + total);
        tvPregunta.setText(pregunta.getPregunta());
        ivPregunta.setImageResource(pregunta.getIdImagen());

        rb1.setText(pregunta.getOpcionA());
        rb2.setText(pregunta.getOpcionB());
        rb3.setText(pregunta.getOpcionC());
        rb4.setText(pregunta.getOpcionD());

        resetearOpciones();
        configurarListeners(pregunta);
    }

    private void resetearOpciones() {
        rgOpciones.clearCheck();
        habilitarOpciones(true);
        esperandoSiguiente = false;

        rb1.setBackgroundResource(R.drawable.radio_button_selector);
        rb2.setBackgroundResource(R.drawable.radio_button_selector);
        rb3.setBackgroundResource(R.drawable.radio_button_selector);
        rb4.setBackgroundResource(R.drawable.radio_button_selector);

        btnSiguiente.setEnabled(false);
    }

    private void configurarListeners(Pregunta pregunta) {
        RadioButton[] botones = { rb1, rb2, rb3, rb4 };

        for (int i = 0; i < botones.length; i++) {
            final int opcion = i + 1;
            final int indice = i;

            botones[i].setOnClickListener(v -> {
                if (esperandoSiguiente)
                    return;

                habilitarOpciones(false);
                esperandoSiguiente = true;

                quizController.responder(opcion, new QuizController.QuizCallback() {
                    @Override
                    public void onPreguntaCargada(Pregunta p, int idx, int total) {
                    }

                    @Override
                    public void onRespuestaCorrecta(int aciertos) {
                        botones[indice].setBackgroundResource(R.drawable.radio_correct);
                        btnSiguiente.setEnabled(true);
                    }

                    @Override
                    public void onRespuestaIncorrecta(int respuestaCorrecta, int aciertos) {
                        botones[indice].setBackgroundResource(R.drawable.radio_incorrect);
                        botones[respuestaCorrecta - 1].setBackgroundResource(R.drawable.radio_checked);
                        btnSiguiente.setEnabled(true);
                    }

                    @Override
                    public void onQuizFinalizado(int aciertos, int total, int puntos) {
                    }

                    @Override
                    public void onError(String mensaje) {
                        Toast.makeText(QuizActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    private void avanzarSiguiente() {
        quizController.siguientePregunta(new QuizController.QuizCallback() {
            @Override
            public void onPreguntaCargada(Pregunta pregunta, int indice, int total) {
                mostrarPreguntaActual();
            }

            @Override
            public void onRespuestaCorrecta(int aciertos) {
            }

            @Override
            public void onRespuestaIncorrecta(int respuestaCorrecta, int aciertos) {
            }

            @Override
            public void onQuizFinalizado(int aciertos, int total, int puntosGanados) {
                mostrarResultadoFinal(aciertos, total, puntosGanados);
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(QuizActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalizarQuiz() {
        int aciertos = quizController.getAciertos();
        int total = quizController.getTotalPreguntas();
        int puntos = quizController.getPuntosGanados();
        mostrarResultadoFinal(aciertos, total, puntos);
    }

    private void mostrarResultadoFinal(int aciertos, int total, int puntosGanados) {
        quizController.guardarProgresoLocal(this);

        String texto = "🎉 ¡Completado!\n" +
                quizController.getNombreTema() + "\n" +
                aciertos + "/" + total + " correctas\n" +
                "+" + puntosGanados + " puntos";

        tvNumeroPregunta.setText("Resultado Final");
        tvPregunta.setText(texto);
        ivPregunta.setVisibility(View.GONE);
        rgOpciones.setVisibility(View.GONE);
        btnSiguiente.setText("Volver");
        btnSiguiente.setEnabled(true);
    }

    private void habilitarOpciones(boolean habilitar) {
        rb1.setEnabled(habilitar);
        rb2.setEnabled(habilitar);
        rb3.setEnabled(habilitar);
        rb4.setEnabled(habilitar);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
