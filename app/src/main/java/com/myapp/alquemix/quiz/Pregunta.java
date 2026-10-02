package com.myapp.alquemix.quiz;

/**
 * Modelo de Pregunta - Módulo de Quiz
 */
public class Pregunta {
    private String pregunta;
    private int idImagen;
    private String opcionA, opcionB, opcionC, opcionD;
    private int respuestaCorrecta; // 1=A, 2=B, 3=C, 4=D

    public Pregunta(String pregunta, int idImagen, String opcionA, String opcionB,
            String opcionC, String opcionD, int respuestaCorrecta) {
        this.pregunta = pregunta;
        this.idImagen = idImagen;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    // Getters
    public String getPregunta() {
        return pregunta;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public String getOpcionA() {
        return opcionA;
    }

    public String getOpcionB() {
        return opcionB;
    }

    public String getOpcionC() {
        return opcionC;
    }

    public String getOpcionD() {
        return opcionD;
    }

    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public String getOpcion(int index) {
        switch (index) {
            case 1:
                return opcionA;
            case 2:
                return opcionB;
            case 3:
                return opcionC;
            case 4:
                return opcionD;
            default:
                return null;
        }
    }

    public boolean esCorrecta(int respuesta) {
        return respuesta == respuestaCorrecta;
    }
}
