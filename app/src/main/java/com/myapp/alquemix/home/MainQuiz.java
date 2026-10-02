package com.myapp.alquemix.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.alquemix.R;
import com.myapp.alquemix.quiz.QuizActivity;

/**
 * Lista de Quizzes - Módulo Home
 * Muestra el progreso de quizzes por usuario
 */
public class MainQuiz extends AppCompatActivity {

    private LinearLayout contenedor;
    private SharedPreferences prefs;
    private String userKey = ""; // Prefix for per-user progress

    private final String[] titulos = {
            "Introducción a los Hidrocarburos",
            "Alcanos",
            "Alquenos",
            "Alquinos",
            "Alcoholes",
            "Reacciones Químicas"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);
        contenedor = findViewById(R.id.contenedor_quizzes);
        prefs = getSharedPreferences("AlquemixPrefs", MODE_PRIVATE);

        // Get user UID for per-user progress
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userKey = currentUser.getUid() + "_";
        } else {
            userKey = "guest_";
        }

        findViewById(R.id.btn_volver).setOnClickListener(v -> finish());
        cargarQuizzes();
    }

    private void cargarQuizzes() {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < titulos.length; i++) {
            View item = inflater.inflate(R.layout.item_quiz, contenedor, false);

            TextView tvNumero = item.findViewById(R.id.tv_numero);
            TextView tvTitulo = item.findViewById(R.id.tv_titulo);
            TextView tvProgreso = item.findViewById(R.id.tv_progreso);
            ProgressBar progressBar = item.findViewById(R.id.progress_bar);
            ImageButton btnPlay = item.findViewById(R.id.btn_play);

            final int temaId = i + 1;
            final String temaNombre = titulos[i];

            tvNumero.setText(String.valueOf(temaId));
            tvTitulo.setText(temaNombre);

            // Check if previous theme is completed (per-user)
            boolean desbloqueado = true;
            if (temaId > 1) {
                int aciertosPrevios = prefs.getInt(userKey + "tema_" + (temaId - 1) + "_aciertos", 0);
                desbloqueado = (aciertosPrevios >= 3); // Unlock if scored at least 3/5 (60%)
            }

            // Get current theme progress (per-user)
            int aciertos = prefs.getInt(userKey + "tema_" + temaId + "_aciertos", 0);
            int porcentaje = aciertos * 20; // 5 questions = 20% each

            if (!desbloqueado) {
                btnPlay.setEnabled(false);
                btnPlay.setAlpha(0.4f);
                tvProgreso.setText("🔒 Bloqueado");
                tvProgreso.setTextColor(getColor(android.R.color.darker_gray));
                progressBar.setProgress(0);
            } else {
                tvProgreso.setText(porcentaje + "% completado");
                progressBar.setProgress(porcentaje);

                if (porcentaje >= 100) {
                    progressBar.setProgressTintList(getColorStateList(android.R.color.holo_green_dark));
                    tvProgreso.setTextColor(getColor(android.R.color.holo_green_dark));
                    tvProgreso.setText("✅ " + porcentaje + "% completado");
                } else if (porcentaje > 0) {
                    tvProgreso.setTextColor(getColor(android.R.color.holo_orange_dark));
                } else {
                    tvProgreso.setTextColor(getColor(android.R.color.darker_gray));
                }
            }

            btnPlay.setOnClickListener(v -> {
                Intent intent = new Intent(MainQuiz.this, QuizActivity.class);
                intent.putExtra("TEMA_ID", temaId);
                intent.putExtra("TEMA_NOMBRE", temaNombre);
                startActivity(intent);
            });

            contenedor.addView(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contenedor.removeAllViews();
        cargarQuizzes();
    }
}
