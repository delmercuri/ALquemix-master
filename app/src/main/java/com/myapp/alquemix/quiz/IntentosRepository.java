package com.myapp.alquemix.quiz;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de Intentos - Módulo de Quiz
 */
public class IntentosRepository {

    private static IntentosRepository instance;
    private final FirebaseFirestore db;

    private IntentosRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized IntentosRepository getInstance() {
        if (instance == null) {
            instance = new IntentosRepository();
        }
        return instance;
    }

    public interface Callback<T> {
        void onSuccess(T result);

        void onError(Exception e);
    }

    public void guardarIntento(String uid, Intento intento, Callback<String> callback) {
        db.collection("usuarios")
                .document(uid)
                .collection("intentos")
                .add(intento.toMap())
                .addOnSuccessListener(docRef -> {
                    if (callback != null)
                        callback.onSuccess(docRef.getId());
                })
                .addOnFailureListener(e -> {
                    if (callback != null)
                        callback.onError(e);
                });
    }

    public void getIntentos(String uid, Callback<List<Intento>> callback) {
        db.collection("usuarios")
                .document(uid)
                .collection("intentos")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Intento> intentos = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Intento i = Intento.fromDocument(doc);
                        if (i != null)
                            intentos.add(i);
                    }
                    callback.onSuccess(intentos);
                })
                .addOnFailureListener(callback::onError);
    }

    public void getIntentosPorTema(String uid, int temaId, Callback<List<Intento>> callback) {
        db.collection("usuarios")
                .document(uid)
                .collection("intentos")
                .whereEqualTo("temaId", temaId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Intento> intentos = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Intento i = Intento.fromDocument(doc);
                        if (i != null)
                            intentos.add(i);
                    }
                    callback.onSuccess(intentos);
                })
                .addOnFailureListener(callback::onError);
    }
}
