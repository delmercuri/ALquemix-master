package com.myapp.alquemix.auth;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelo de Usuario - Módulo de Autenticación
 */
public class Usuario {
    private String uid;
    private String nombres;
    private String apellidos;
    private String dni;
    private String email;
    private String rol;
    private int puntos;
    private String nivel;

    public Usuario() {
    }

    public Usuario(String uid, String nombres, String apellidos, String dni,
            String email, String rol, int puntos, String nivel) {
        this.uid = uid;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.rol = rol;
        this.puntos = puntos;
        this.nivel = nivel;
    }

    public static Usuario fromDocument(DocumentSnapshot doc) {
        if (doc == null || !doc.exists())
            return null;

        Usuario usuario = new Usuario();
        usuario.uid = doc.getId();
        usuario.nombres = doc.getString("nombres");
        usuario.apellidos = doc.getString("apellidos");
        usuario.dni = doc.getString("dni");
        usuario.email = doc.getString("email");
        usuario.rol = doc.getString("rol");
        usuario.nivel = doc.getString("nivel");

        Long pts = doc.getLong("puntos");
        usuario.puntos = pts != null ? pts.intValue() : 0;

        return usuario;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("nombres", nombres);
        map.put("apellidos", apellidos);
        map.put("dni", dni);
        map.put("email", email);
        map.put("rol", rol);
        map.put("puntos", puntos);
        map.put("nivel", nivel);

        Map<String, Object> progreso = new HashMap<>();
        progreso.put("temasCompletados", 0);
        progreso.put("totalTemas", 6);
        progreso.put("porcentaje", 0);
        progreso.put("rachaActual", 0);
        progreso.put("mejorRacha", 0);
        map.put("progreso", progreso);

        Map<String, Object> quizzes = new HashMap<>();
        quizzes.put("completados", 0);
        quizzes.put("puntajeTotal", 0);
        quizzes.put("mejorPuntaje", 0);
        map.put("quizzes", quizzes);

        return map;
    }

    // Getters
    public String getUid() {
        return uid;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getNivel() {
        return nivel;
    }

    public String getNombreCompleto() {
        return (nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "");
    }

    public String getIniciales() {
        String n = nombres != null && !nombres.isEmpty() ? nombres.substring(0, 1) : "?";
        String a = apellidos != null && !apellidos.isEmpty() ? apellidos.substring(0, 1) : "?";
        return n + a;
    }

    public boolean esDocente() {
        return "docente".equals(rol);
    }

    // Setters
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}
