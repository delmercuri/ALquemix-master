package com.myapp.alquemix.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.myapp.alquemix.R
import com.myapp.alquemix.auth.RegisterActivity
import com.myapp.alquemix.profile.ProfileActivity
import com.myapp.alquemix.teacher.TopActivity

import com.squareup.picasso.Picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var tvSaludo: TextView

    private lateinit var imgPerfil:
            de.hdodenhof.circleimageview.CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        db = FirebaseFirestore.getInstance()

        tvSaludo =
            findViewById(R.id.tv_saludo)

        imgPerfil =
            findViewById(R.id.img_perfil)

        cargarNombreUsuario()

        // Abrir actualizar perfil
        imgPerfil.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ActualizarPerfilActivity::class.java
                )
            )
        }

        findViewById<LinearLayout>(
            R.id.btn_temario
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MenuTemasActivity::class.java
                )
            )
        }

        findViewById<LinearLayout>(
            R.id.btn_quiz
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainQuiz::class.java
                )
            )
        }

        findViewById<LinearLayout>(
            R.id.btn_estadisticas
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }

        findViewById<LinearLayout>(
            R.id.btn_top
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    TopActivity::class.java
                )
            )
        }

        findViewById<Button>(
            R.id.btn_salir
        ).setOnClickListener {

            auth.signOut()

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )

            finish()
        }
    }

    override fun onResume() {

        super.onResume()

        // Cada vez que regresamos de actualizar perfil
        // vuelve a consultar Firebase.
        cargarNombreUsuario()
    }

    private fun cargarNombreUsuario() {

        val usuario =
            auth.currentUser

        if (usuario == null) {

            tvSaludo.text =
                "¡Hola, Usuario!"

            return
        }

        val uid =
            usuario.uid

        db.collection("usuarios")
            .document(uid)
            .get()

            .addOnSuccessListener { doc ->

                val nombre =
                    doc.getString("nombres")
                        ?: "Usuario"

                val apellido =
                    doc.getString("apellidos")
                        ?: ""

                tvSaludo.text =
                    "¡Hola, $nombre $apellido!"

                // =====================================
                // CARGAR FOTO DE PERFIL
                // =====================================

                val fotoPerfil =
                    doc.getString("fotoPerfil")

                if (!fotoPerfil.isNullOrEmpty()) {

                    // Limpiar caché de esa URL
                    Picasso.get()
                        .invalidate(fotoPerfil)

                    // Descargar directamente desde red
                    Picasso.get()
                        .load(fotoPerfil)
                        .memoryPolicy(
                            MemoryPolicy.NO_CACHE,
                            MemoryPolicy.NO_STORE
                        )
                        .networkPolicy(
                            NetworkPolicy.NO_CACHE
                        )
                        .placeholder(
                            android.R.drawable.ic_menu_myplaces
                        )
                        .error(
                            android.R.drawable.ic_menu_myplaces
                        )
                        .into(imgPerfil)

                } else {

                    // No hay foto guardada
                    imgPerfil.setImageResource(
                        android.R.drawable.ic_menu_myplaces
                    )
                }
            }

            .addOnFailureListener {

                tvSaludo.text =
                    "¡Hola, Usuario!"
            }
    }
}