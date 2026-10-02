package com.myapp.alquemix.quiz;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelo de Intento de Quiz - Módulo de Quiz
 */
public class Intento {
    private String id;
    private int correctas;
    private int totalPreguntas;
    private String tema;
    private int temaId;
    private Timestamp fecha;
    private int porcentaje;

    public Intento() {
    }

    public Intento(int correctas, int totalPreguntas, String tema, int temaId) {
        this.correctas = correctas;
        this.totalPreguntas = totalPreguntas;
        this.tema = tema;
        this.temaId = temaId;
        this.porcentaje = totalPreguntas > 0 ? (correctas * 100 / totalPreguntas) : 0;
    }

    public static Intento fromDocument(DocumentSnapshot doc) {
        if (doc == null || !doc.exists())
            return null;

        Intento intento = new Intento();
        intento.id = doc.getId();

        Long c = doc.getLong("correctas");
        Long t = doc.getLong("totalPreguntas");
        Long tid = doc.getLong("temaId");
        Long p = doc.getLong("porcentaje");

        intento.correctas = c != null ? c.intValue() : 0;
        intento.totalPreguntas = t != null ? t.intValue() : 0;
        intento.temaId = tid != null ? tid.intValue() : 0;
        intento.porcentaje = p != null ? p.intValue() : 0;
        intento.tema = doc.getString("tema");
        intento.fecha = doc.getTimestamp("fecha");

        return intento;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("correctas", correctas);
        map.put("totalPreguntas", totalPreguntas);
        map.put("tema", tema);
        map.put("temaId", temaId);
        map.put("fecha", FieldValue.serverTimestamp());
        map.put("porcentaje", porcentaje);
        return map;
    }

    // Getters
    public String getId() {
        return id;
    }

    public int getCorrectas() {
        return correctas;
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public String getTema() {
        return tema;
    }

    public int getTemaId() {
        return temaId;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public int getPorcentaje() {
        return porcentaje;
    }
}
