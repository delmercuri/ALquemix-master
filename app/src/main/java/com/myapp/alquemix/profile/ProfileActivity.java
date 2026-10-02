package com.myapp.alquemix.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.alquemix.R;
import com.myapp.alquemix.auth.Usuario;
import com.myapp.alquemix.home.ActualizarPerfilActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvNombre;
    private TextView tvNombres;
    private TextView tvApellidos;
    private TextView tvCorreo;
    private TextView tvDni;
    private TextView tvRol;
    private TextView tvNivel;
    private TextView tvPuntos;

    private Button btnEditarPerfil;

    private ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileController = ProfileController.getInstance();

        inicializarVistas();
        cargarPerfil();
    }

    private void inicializarVistas() {

        tvNombre = findViewById(R.id.tv_nombre_profile);
        tvNombres = findViewById(R.id.tv_nombres);
        tvApellidos = findViewById(R.id.tv_apellidos);
        tvCorreo = findViewById(R.id.tv_correo);
        tvDni = findViewById(R.id.tv_dni);
        tvRol = findViewById(R.id.tv_rol);
        tvNivel = findViewById(R.id.tv_nivel);
        tvPuntos = findViewById(R.id.tv_puntos_profile);

        btnEditarPerfil = findViewById(R.id.btn_editar_perfil);

        btnEditarPerfil.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    ActualizarPerfilActivity.class
            );

            startActivity(intent);
        });
    }

    private void cargarPerfil() {

        profileController.cargarPerfil(
                new ProfileController.ProfileCallback() {

                    @Override
                    public void onDatosCargados(
                            Usuario usuario,
                            EstadisticasUsuario estadisticas) {

                        mostrarDatosUsuario(usuario);
                    }

                    @Override
                    public void onError(String mensaje) {

                        Toast.makeText(
                                ProfileActivity.this,
                                mensaje,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void mostrarDatosUsuario(Usuario usuario) {

        if (usuario == null) {
            return;
        }

        String nombres = usuario.getNombres();
        String apellidos = usuario.getApellidos();

        if (nombres == null) {
            nombres = "";
        }

        if (apellidos == null) {
            apellidos = "";
        }

        String nombreCompleto =
                (nombres + " " + apellidos).trim();

        tvNombre.setText(
                nombreCompleto.isEmpty()
                        ? "Usuario"
                        : nombreCompleto
        );

        tvNombres.setText(
                nombres.isEmpty()
                        ? "No registrado"
                        : nombres
        );

        tvApellidos.setText(
                apellidos.isEmpty()
                        ? "No registrado"
                        : apellidos
        );

        tvCorreo.setText(
                usuario.getEmail() != null
                        ? usuario.getEmail()
                        : "No registrado"
        );

        tvDni.setText(
                usuario.getDni() != null
                        ? usuario.getDni()
                        : "No registrado"
        );

        tvRol.setText(
                usuario.getRol() != null
                        ? usuario.getRol()
                        : "Estudiante"
        );

        tvNivel.setText(
                usuario.getNivel() != null
                        ? usuario.getNivel()
                        : "Nivel 1"
        );

        tvPuntos.setText(
                String.valueOf(usuario.getPuntos())
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (profileController != null) {
            cargarPerfil();
        }
    }
}