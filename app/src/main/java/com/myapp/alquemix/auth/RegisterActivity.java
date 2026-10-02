package com.myapp.alquemix.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.alquemix.R;

/**
 * Vista de Registro - Módulo de Autenticación
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText etNombres, etApellidos, etDni, etEmail, etPassword, etClaveDocente;
    private RadioGroup rgRol;
    private View layoutClaveDocente;
    private Button btnRegistrar;
    private TextView tvLogin;

    private AuthController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authController = AuthController.getInstance();

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNombres = findViewById(R.id.et_nombres);
        etApellidos = findViewById(R.id.et_apellidos);
        etDni = findViewById(R.id.et_dni);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etClaveDocente = findViewById(R.id.et_clave_docente);
        rgRol = findViewById(R.id.rg_rol);
        layoutClaveDocente = findViewById(R.id.layout_clave_docente);
        btnRegistrar = findViewById(R.id.btn_registrar);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setupListeners() {
        rgRol.setOnCheckedChangeListener((group, checkedId) -> layoutClaveDocente.setVisibility(
                checkedId == R.id.rb_docente ? View.VISIBLE : View.GONE));

        btnRegistrar.setOnClickListener(v -> registrar());
        tvLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void registrar() {
        String nombres = etNombres.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String dni = etDni.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        boolean esDocente = rgRol.getCheckedRadioButtonId() == R.id.rb_docente;
        String claveDocente = etClaveDocente.getText().toString();

        setLoading(true);

        authController.registrar(nombres, apellidos, dni, email, password,
                esDocente, claveDocente, new AuthController.AuthCallback() {
                    @Override
                    public void onSuccess(Usuario usuario) {
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String mensaje) {
                        setLoading(false);
                        Toast.makeText(RegisterActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoading(boolean loading) {
        btnRegistrar.setEnabled(!loading);
        btnRegistrar.setText(loading ? "Registrando..." : "REGISTRARSE");
    }
}
