package com.myapp.alquemix.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myapp.alquemix.R;
import com.squareup.picasso.Picasso;

public class ActualizarPerfilActivity extends AppCompatActivity {

    private EditText etNombres;
    private EditText etCorreo;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private ImageView imgPerfil;

    private Button btnCambiarFoto;
    private Button btnGuardar;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    private String uid;

    private Uri imagenSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actualizar_perfil);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseUser usuario = auth.getCurrentUser();

        if (usuario == null) {

            Toast.makeText(
                    this,
                    "Usuario no autenticado",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        uid = usuario.getUid();

        etNombres = findViewById(R.id.et_nombres);
        etCorreo = findViewById(R.id.et_correo);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        imgPerfil = findViewById(R.id.img_perfil);

        btnCambiarFoto = findViewById(R.id.btn_cambiar_foto);
        btnGuardar = findViewById(R.id.btn_guardar);

        cargarDatos();

        btnCambiarFoto.setOnClickListener(v -> seleccionarFoto());

        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    // ============================================================
    // CARGAR DATOS DEL PERFIL
    // ============================================================

    private void cargarDatos() {

        db.collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {

                    if (doc.exists()) {

                        String nombres = doc.getString("nombres");
                        String correo = doc.getString("email");
                        String foto = doc.getString("fotoPerfil");

                        if (nombres != null) {
                            etNombres.setText(nombres);
                        }

                        if (correo != null) {
                            etCorreo.setText(correo);
                        }

                        if (foto != null && !foto.isEmpty()) {

                            Picasso.get()
                                    .load(foto)
                                    .placeholder(
                                            android.R.drawable.ic_menu_myplaces
                                    )
                                    .error(
                                            android.R.drawable.ic_menu_myplaces
                                    )
                                    .into(imgPerfil);
                        }
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Error cargando perfil: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    // ============================================================
    // SELECCIONAR FOTO
    // ============================================================

    private void seleccionarFoto() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        startActivityForResult(intent, 100);
    }

    // ============================================================
    // RESULTADO DE GALERÍA
    // ============================================================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == 100
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            imagenSeleccionada = data.getData();

            // Mostrar inmediatamente la imagen seleccionada
            imgPerfil.setImageURI(imagenSeleccionada);

            // Subir a Firebase
            subirFoto();
        }
    }

    // ============================================================
    // SUBIR FOTO A FIREBASE STORAGE
    // ============================================================

    private void subirFoto() {

        if (imagenSeleccionada == null) {
            return;
        }

        // IMPORTANTE:
        // Usamos timestamp para generar un nombre NUEVO.
        // Así la URL cambia y evitamos problemas de caché.

        String nombreArchivo =
                uid + "_" + System.currentTimeMillis() + ".jpg";

        StorageReference referencia =
                storage.getReference()
                        .child("perfil")
                        .child(nombreArchivo);

        Toast.makeText(
                this,
                "Subiendo foto...",
                Toast.LENGTH_SHORT
        ).show();

        referencia.putFile(imagenSeleccionada)

                .addOnSuccessListener(taskSnapshot -> {

                    // Obtener URL de descarga
                    referencia.getDownloadUrl()

                            .addOnSuccessListener(uri -> {

                                String urlFoto =
                                        uri.toString();

                                // Guardar URL en Firestore
                                db.collection("usuarios")
                                        .document(uid)
                                        .update(
                                                "fotoPerfil",
                                                urlFoto
                                        )
                                        .addOnSuccessListener(aVoid -> {

                                            Toast.makeText(
                                                    this,
                                                    "Foto actualizada correctamente",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                        })
                                        .addOnFailureListener(e -> {

                                            Toast.makeText(
                                                    this,
                                                    "Error guardando foto en Firestore: "
                                                            + e.getMessage(),
                                                    Toast.LENGTH_LONG
                                            ).show();

                                        });

                            })

                            .addOnFailureListener(e -> {

                                Toast.makeText(
                                        this,
                                        "Error obteniendo URL: "
                                                + e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();

                            });

                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Error subiendo foto: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });
    }

    // ============================================================
    // GUARDAR NOMBRE, CORREO Y CONTRASEÑA
    // ============================================================

    private void guardarCambios() {

        String nombres =
                etNombres.getText()
                        .toString()
                        .trim();

        String correo =
                etCorreo.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString()
                        .trim();

        String confirmar =
                etConfirmPassword.getText()
                        .toString()
                        .trim();

        if (nombres.isEmpty() || correo.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completa los campos correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validar contraseña antes de actualizar
        if (!password.isEmpty()) {

            if (!password.equals(confirmar)) {

                Toast.makeText(
                        this,
                        "Las contraseñas no coinciden",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (password.length() < 6) {

                Toast.makeText(
                        this,
                        "La contraseña debe tener al menos 6 caracteres",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
        }

        FirebaseUser usuario =
                auth.getCurrentUser();

        if (usuario == null) {
            return;
        }

        // Actualizar correo
        usuario.updateEmail(correo)
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Error actualizando correo: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });

        // Actualizar contraseña si escribió una nueva
        if (!password.isEmpty()) {

            usuario.updatePassword(password)
                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                this,
                                "Error actualizando contraseña: "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    });
        }

        // Actualizar Firestore
        db.collection("usuarios")
                .document(uid)
                .update(
                        "nombres",
                        nombres,
                        "email",
                        correo
                )
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(
                            this,
                            "Perfil actualizado",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Error: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}