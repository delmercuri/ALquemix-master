package com.myapp.alquemix.quiz;

import com.myapp.alquemix.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repositorio de Preguntas - Módulo de Quiz
 */
public class QuizRepository {

    private static QuizRepository instance;

    private QuizRepository() {
    }

    public static synchronized QuizRepository getInstance() {
        if (instance == null) {
            instance = new QuizRepository();
        }
        return instance;
    }

    public List<Pregunta> getPreguntasPorTema(int temaId, boolean shuffle) {
        List<Pregunta> preguntas = new ArrayList<>();

        switch (temaId) {
            case 1:
                preguntas = getIntroHidrocarburos();
                break;
            case 2:
                preguntas = getAlcanos();
                break;
            case 3:
                preguntas = getAlquenos();
                break;
            case 4:
                preguntas = getAlquinos();
                break;
            case 5:
                preguntas = getAlcoholes();
                break;
            case 6:
                preguntas = getReacciones();
                break;
        }

        if (shuffle)
            Collections.shuffle(preguntas);
        return preguntas;
    }

    private List<Pregunta> getIntroHidrocarburos() {
        List<Pregunta> p = new ArrayList<>();
        p.add(new Pregunta("¿Qué son los hidrocarburos?", R.drawable.hc1,
                "Compuestos de C y O", "Solo carbono", "Carbono e hidrógeno", "Con nitrógeno", 3));
        p.add(new Pregunta("Fórmula general de los alcanos", R.drawable.hc2,
                "CₙH₂ₙ", "CₙH₂ₙ₊₂", "CₙH₂ₙ₋₂", "CₙHₙ", 2));
        p.add(new Pregunta("Los hidrocarburos saturados tienen...", R.drawable.hc3,
                "Enlaces dobles", "Solo enlaces simples", "Enlaces triples", "Enlaces iónicos", 2));
        p.add(new Pregunta("Hidrocarburo más simple", R.drawable.hc4,
                "Eteno", "Metano", "Acetileno", "Benceno", 2));
        p.add(new Pregunta("¿Qué significa 'alifático'?", R.drawable.hc5,
                "Anillo aromático", "Cadena abierta", "Cíclico", "Insaturado", 2));
        return p;
    }

    private List<Pregunta> getAlcanos() {
        List<Pregunta> p = new ArrayList<>();
        p.add(new Pregunta("Sufijo de los alcanos", R.drawable.alcano1, "-eno", "-ano", "-ino", "-ol", 2));
        p.add(new Pregunta("¿Cuántos enlaces forma cada carbono?", R.drawable.alcano2, "3", "4", "5", "2", 2));
        p.add(new Pregunta("El butano tiene cuántos carbonos", R.drawable.alcano3, "3", "4", "5", "6", 2));
        p.add(new Pregunta("¿Qué es el isopentano?", R.drawable.alcano4, "Lineal", "Ramificado", "Cíclico", "Aromático",
                2));
        p.add(new Pregunta("Los alcanos son...", R.drawable.alcano5, "Muy reactivos", "Poco reactivos", "Ácidos",
                "Básicos", 2));
        return p;
    }

    private List<Pregunta> getAlquenos() {
        List<Pregunta> p = new ArrayList<>();
        p.add(new Pregunta("Sufijo de los alquenos", R.drawable.alqueno1, "-ano", "-eno", "-ino", "-al", 2));
        p.add(new Pregunta("El eteno también se llama", R.drawable.alqueno2, "Acetileno", "Etileno", "Propino",
                "Metano", 2));
        p.add(new Pregunta("Un doble enlace C=C tiene", R.drawable.alqueno3, "1σ + 1π", "2σ", "2π", "4 enlaces", 1));
        p.add(new Pregunta("Los alquenos decoloran...", R.drawable.alqueno4, "Fenoltaleína", "Agua de bromo",
                "Permanganato", "Las dos", 2));
        p.add(new Pregunta("Geometría del carbono con doble enlace", R.drawable.alqueno5, "Tetraédrica",
                "Trigonal plana", "Lineal", "Octaédrica", 2));
        return p;
    }

    private List<Pregunta> getAlquinos() {
        List<Pregunta> p = new ArrayList<>();
        p.add(new Pregunta("Sufijo de los alquinos", R.drawable.alquino1, "-ano", "-eno", "-ino", "-ol", 3));
        p.add(new Pregunta("El acetileno es...", R.drawable.alquino2, "Eteno", "Etino", "Propino", "Butino", 2));
        p.add(new Pregunta("Un triple enlace tiene", R.drawable.alquino3, "1σ + 2π", "3σ", "3π", "1σ solo", 1));
        p.add(new Pregunta("Los alquinos terminales reaccionan con...", R.drawable.alquino4, "Agua regia",
                "Amoniacal de cobre", "Fenoltaleína", "Solo calor", 2));
        p.add(new Pregunta("Geometría del carbono con triple enlace", R.drawable.alquino5, "Tetraédrica", "Trigonal",
                "Lineal", "Pirámide", 3));
        return p;
    }

    private List<Pregunta> getAlcoholes() {
        List<Pregunta> p = new ArrayList<>();
        p.add(new Pregunta("Sufijo de los alcoholes", R.drawable.alcohol1, "-ano", "-eno", "-ol", "-al", 3));
        p.add(new Pregunta("El etanol es alcohol...", R.drawable.alcohol2, "Secundario", "Primario", "Terciario",
                "Fenólico", 2));
        p.add(new Pregunta("Grupo funcional del fenol", R.drawable.alcohol3, "-OH en cadena", "-OH en anillo", "-O-",
                "-NH2", 2));
        p.add(new Pregunta("Fórmula del metanol", R.drawable.alcohol4, "C₂H₅OH", "CH₃OH", "C₃H₇OH", "C₆H₅OH", 2));
        p.add(new Pregunta("Los alcoholes forman...", R.drawable.alcohol5, "Enlaces iónicos", "Puentes de hidrógeno",
                "No forman nada", "Enlaces metálicos", 2));
        return p;
    }

    private List<Pregunta> getReacciones() {
        List<Pregunta> p = new ArrayList<>();
        p.add(new Pregunta("2H₂ + O₂ → 2H₂O es reacción de", R.drawable.reac1, "Descomposición", "Síntesis",
                "Desplazamiento", "Metátesis", 2));
        p.add(new Pregunta("Reacción que forma sal + agua", R.drawable.reac2, "Combustión", "Neutralización", "Redox",
                "Descomposición", 2));
        p.add(new Pregunta("Símbolo (aq) significa", R.drawable.reac3, "Gas", "Líquido", "Disuelto en agua", "Sólido",
                3));
        p.add(new Pregunta("Reacción que libera calor", R.drawable.reac4, "Endotérmica", "Exotérmica", "Isotérmica",
                "Ninguna", 2));
        p.add(new Pregunta("Sustancia que acelera sin consumirse", R.drawable.reac5, "Reactivo", "Catalizador",
                "Producto", "Intermedio", 2));
        return p;
    }

    public int getCantidadPreguntas(int temaId) {
        return getPreguntasPorTema(temaId, false).size();
    }
}
