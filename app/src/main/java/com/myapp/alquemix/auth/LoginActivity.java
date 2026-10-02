package com.myapp.alquemix.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.alquemix.R;
import com.myapp.alquemix.home.MainActivity;
import com.myapp.alquemix.teacher.TeacherActivity;

/**
 * Vista de Login - Módulo de Autenticación
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegistro;

    private AuthController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authController = AuthController.getInstance();

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegistro = findViewById(R.id.tv_registro);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> login());
        tvRegistro.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        setLoading(true);

        authController.login(email, password, new AuthController.AuthCallback() {
            @Override
            public void onSuccess(Usuario usuario) {
                navegarSegunRol(usuario);
            }

            @Override
            public void onError(String mensaje) {
                setLoading(false);
                System.err.println("Login Error: " + mensaje);
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navegarSegunRol(Usuario usuario) {
        Class<?> destino = usuario.esDocente() ? TeacherActivity.class : MainActivity.class;

        startActivity(new Intent(this, destino)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        btnLogin.setText(loading ? "Entrando..." : "ENTRAR");
    }
}
