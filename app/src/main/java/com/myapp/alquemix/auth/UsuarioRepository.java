package com.myapp.alquemix.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de Usuarios - Módulo de Autenticación
 */
public class UsuarioRepository {

    private static UsuarioRepository instance;
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    private UsuarioRepository() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized UsuarioRepository getInstance() {
        if (instance == null) {
            instance = new UsuarioRepository();
        }
        return instance;
    }

    public interface Callback<T> {
        void onSuccess(T result);

        void onError(Exception e);
    }

    public void login(String email, String password, Callback<Usuario> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    getUsuario(uid, callback);
                })
                .addOnFailureListener(callback::onError);
    }

    public void register(Usuario usuario, String password, Callback<Usuario> callback) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    usuario.setUid(uid);

                    db.collection("usuarios")
                            .document(uid)
                            .set(usuario.toMap())
                            .addOnSuccessListener(aVoid -> callback.onSuccess(usuario))
                            .addOnFailureListener(callback::onError);
                })
                .addOnFailureListener(callback::onError);
    }

    public void getUsuario(String uid, Callback<Usuario> callback) {
        db.collection("usuarios").document(uid).get()
                .addOnSuccessListener(doc -> {
                    Usuario usuario = Usuario.fromDocument(doc);
                    callback.onSuccess(usuario);
                })
                .addOnFailureListener(callback::onError);
    }

    public void getUsuarioActual(Callback<Usuario> callback) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            callback.onError(new Exception("No hay usuario autenticado"));
            return;
        }
        getUsuario(currentUser.getUid(), callback);
    }

    public void getUsuariosByRol(String rol, int limit, Callback<List<Usuario>> callback) {
        db.collection("usuarios")
                .whereEqualTo("rol", rol)
                .limit(limit)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Usuario> usuarios = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Usuario u = Usuario.fromDocument(doc);
                        if (u != null)
                            usuarios.add(u);
                    }
                    callback.onSuccess(usuarios);
                })
                .addOnFailureListener(callback::onError);
    }

    public void getUsuariosByRol(String rol, Callback<List<Usuario>> callback) {
        getUsuariosByRol(rol, 100, callback); // Default limit of 100 to avoid excessive reads
    }

    public void getEstudiantes(int limit, Callback<List<Usuario>> callback) {
        getUsuariosByRol("estudiante", limit, callback);
    }

    public void getEstudiantes(Callback<List<Usuario>> callback) {
        getEstudiantes(50, callback); // Default limit for general use
    }

    public void agregarPuntos(String uid, int puntos) {
        db.collection("usuarios").document(uid)
                .update("puntos", FieldValue.increment(puntos));
    }

    public void logout() {
        auth.signOut();
    }

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public String getCurrentUid() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}
