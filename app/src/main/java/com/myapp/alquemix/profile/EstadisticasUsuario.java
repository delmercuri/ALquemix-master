package com.myapp.alquemix.profile;

/**
 * Modelo de Estadísticas - Módulo de Perfil
 */
public class EstadisticasUsuario {
    private int temasCompletados;
    private int totalTemas = 6;
    private int porcentajeProgreso;
    private int rachaActual;
    private int mejorRacha;
    private int quizzesCompletados;
    private int puntajePromedio;
    private int mejorPuntaje;

    public EstadisticasUsuario() {
    }

    // Getters
    public int getTemasCompletados() {
        return temasCompletados;
    }

    public int getTotalTemas() {
        return totalTemas;
    }

    public int getPorcentajeProgreso() {
        return porcentajeProgreso;
    }

    public int getRachaActual() {
        return rachaActual;
    }

    public int getMejorRacha() {
        return mejorRacha;
    }

    public int getQuizzesCompletados() {
        return quizzesCompletados;
    }

    public int getPuntajePromedio() {
        return puntajePromedio;
    }

    public int getMejorPuntaje() {
        return mejorPuntaje;
    }

    // Setters
    public void setTemasCompletados(int v) {
        temasCompletados = v;
    }

    public void setTotalTemas(int v) {
        totalTemas = v;
    }

    public void setPorcentajeProgreso(int v) {
        porcentajeProgreso = v;
    }

    public void setRachaActual(int v) {
        rachaActual = v;
    }

    public void setMejorRacha(int v) {
        mejorRacha = v;
    }

    public void setQuizzesCompletados(int v) {
        quizzesCompletados = v;
    }

    public void setPuntajePromedio(int v) {
        puntajePromedio = v;
    }

    public void setMejorPuntaje(int v) {
        mejorPuntaje = v;
    }

    // Formatters para UI
    public String getTemasCompletadosText() {
        return temasCompletados + "/" + totalTemas;
    }

    public String getPorcentajeProgresoText() {
        return porcentajeProgreso + "%";
    }

    public String getRachaActualText() {
        return rachaActual + " días";
    }

    public String getMejorRachaText() {
        return mejorRacha + " días";
    }

    public String getPuntajePromedioText() {
        return puntajePromedio + "%";
    }

    public String getMejorPuntajeText() {
        return mejorPuntaje + "%";
    }
}
