package com.myapp.alquemix.auth;

/**
 * Controlador de Autenticación - Módulo de Autenticación
 */
public class AuthController {

    private static AuthController instance;
    private final UsuarioRepository usuarioRepository;

    private static final String CLAVE_DOCENTE = "ALQUEMIX2025";

    private AuthController() {
        usuarioRepository = UsuarioRepository.getInstance();
    }

    public static synchronized AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    public interface AuthCallback {
        void onSuccess(Usuario usuario);

        void onError(String mensaje);
    }

    public void login(String email, String password, AuthCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            callback.onError("El email es requerido");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            callback.onError("La contraseña es requerida");
            return;
        }

        usuarioRepository.login(email.trim(), password, new UsuarioRepository.Callback<Usuario>() {
            @Override
            public void onSuccess(Usuario usuario) {
                callback.onSuccess(usuario);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(traducirError(e.getMessage()));
            }
        });
    }

    public void registrar(String nombres, String apellidos, String dni,
            String email, String password, boolean esDocente,
            String claveDocente, AuthCallback callback) {

        if (nombres == null || nombres.trim().isEmpty()) {
            callback.onError("Los nombres son requeridos");
            return;
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            callback.onError("Los apellidos son requeridos");
            return;
        }
        if (dni == null || dni.length() != 8) {
            callback.onError("DNI debe tener 8 dígitos");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            callback.onError("El email es requerido");
            return;
        }
        if (password == null || password.length() < 6) {
            callback.onError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        if (esDocente && !CLAVE_DOCENTE.equals(claveDocente)) {
            callback.onError("Clave de docente incorrecta");
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(nombres.trim());
        usuario.setApellidos(apellidos.trim());
        usuario.setDni(dni.trim());
        usuario.setEmail(email.trim());
        usuario.setRol(esDocente ? "docente" : "estudiante");
        usuario.setPuntos(0);
        usuario.setNivel("Principiante");

        usuarioRepository.register(usuario, password, new UsuarioRepository.Callback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(traducirError(e.getMessage()));
            }
        });
    }

    public void logout() {
        usuarioRepository.logout();
    }

    public boolean isLoggedIn() {
        return usuarioRepository.isLoggedIn();
    }

    public void getUsuarioActual(AuthCallback callback) {
        usuarioRepository.getUsuarioActual(new UsuarioRepository.Callback<Usuario>() {
            @Override
            public void onSuccess(Usuario usuario) {
                callback.onSuccess(usuario);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    private String traducirError(String error) {
        if (error == null)
            return "Error desconocido";

        if (error.contains("email address is badly formatted")) {
            return "El formato del email es incorrecto";
        }
        if (error.contains("password is invalid") || error.contains("wrong password")) {
            return "Contraseña incorrecta";
        }
        if (error.contains("no user record")) {
            return "No existe una cuenta con este email";
        }
        if (error.contains("email address is already in use")) {
            return "Ya existe una cuenta con este email";
        }
        if (error.contains("network error")) {
            return "Error de conexión. Verifica tu internet";
        }
        if (error.contains("QUOTA_EXCEEDED")) {
            return "Límite de servicio excedido (Quota Exceeded). Contacta al administrador.";
        }
        if (error.contains("too many requests")) {
            return "Demasiados intentos. Inténtalo más tarde.";
        }
        if (error.contains("PROJECT_LIMIT_REACHED")) {
            return "Límite de proyecto alcanzado. Verifica tu configuración de Firebase.";
        }

        return error;
    }
}
