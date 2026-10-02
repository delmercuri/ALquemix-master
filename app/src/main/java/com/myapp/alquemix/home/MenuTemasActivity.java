package com.myapp.alquemix.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.myapp.alquemix.R;
import com.myapp.alquemix.profile.ProfileActivity;

/**
 * Menú de Temas - Módulo Home
 */
public class MenuTemasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_temas);

        // Botón de regreso
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Foto de perfil clickeable - navega a ProfileActivity
        CardView cardProfile = findViewById(R.id.card_profile);
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Configurar temas
        configurarTema(R.id.tag_1, 1, "Introducción a los Hidrocarburos");
        configurarTema(R.id.tag_2, 2, "Alcanos");
        configurarTema(R.id.tag_3, 3, "Alquenos");
        configurarTema(R.id.tag_4, 4, "Alquinos");
        configurarTema(R.id.tag_5, 5, "Alcoholes");
        configurarTema(R.id.tag_6, 6, "Reacciones Químicas");
    }

    private void configurarTema(int viewId, int temaId, String nombreTema) {
        LinearLayout tema = findViewById(viewId);
        tema.setOnClickListener(v -> {
            Intent i = new Intent(MenuTemasActivity.this, InfoActivity.class);
            i.putExtra("TEMA_ID", temaId);
            i.putExtra("TEMA_NOMBRE", nombreTema);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
