package com.myapp.alquemix.teacher;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.alquemix.R;
import com.myapp.alquemix.auth.AuthController;
import com.myapp.alquemix.auth.LoginActivity;
import com.myapp.alquemix.teacher.RankingController.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Vista de Docente - Módulo de Docente
 */
public class TeacherActivity extends AppCompatActivity {

    private LinearLayout contenedorVista;
    private Button btnEstadisticas, btnRanking;
    private ImageButton btnCerrarSesion;

    private RankingController rankingController;
    private AuthController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        rankingController = RankingController.getInstance();
        authController = AuthController.getInstance();

        initViews();
        setupListeners();
        mostrarEstadisticas();
    }

    private void initViews() {
        contenedorVista = findViewById(R.id.contenedor_vista);
        btnEstadisticas = findViewById(R.id.btn_estadisticas);
        btnRanking = findViewById(R.id.btn_ranking);
        btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);
    }

    private void setupListeners() {
        btnCerrarSesion.setOnClickListener(v -> {
            authController.logout();
            startActivity(new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });

        btnEstadisticas.setOnClickListener(v -> {
            btnEstadisticas.setTextColor(0xFF006400);
            btnRanking.setTextColor(0xFF666666);
            mostrarEstadisticas();
        });

        btnRanking.setOnClickListener(v -> {
            btnEstadisticas.setTextColor(0xFF666666);
            btnRanking.setTextColor(0xFF006400);
            mostrarRanking();
        });
    }

    private void mostrarEstadisticas() {
        contenedorVista.removeAllViews();
        View vista = LayoutInflater.from(this).inflate(R.layout.vista_estadisticas, contenedorVista, false);
        contenedorVista.addView(vista);

        TextView tvTotal = vista.findViewById(R.id.tv_total_alumnos);
        TextView tvPromedio = vista.findViewById(R.id.tv_promedio_general);
        RecyclerView rvAlumnos = vista.findViewById(R.id.rv_lista_alumnos);
        RecyclerView rvTemas = vista.findViewById(R.id.rv_promedio_temas);

        rvAlumnos.setLayoutManager(new LinearLayoutManager(this));
        rvTemas.setLayoutManager(new LinearLayoutManager(this));

        rankingController.cargarEstadisticasDocente(new EstadisticasDocenteCallback() {
            @Override
            public void onEstadisticasCargadas(int totalEstudiantes, int promedioGeneral,
                    List<EstudianteRanking> alumnos, List<EstadisticasTema> temas) {
                tvTotal.setText(String.valueOf(totalEstudiantes));
                tvPromedio.setText(promedioGeneral + "%");
                rvAlumnos.setAdapter(new AlumnosAdapter(alumnos));
                rvTemas.setAdapter(new TemasAdapter(temas));
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(TeacherActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarRanking() {
        contenedorVista.removeAllViews();
        View vista = LayoutInflater.from(this).inflate(R.layout.vista_ranking, contenedorVista, false);
        contenedorVista.addView(vista);

        RecyclerView rv = vista.findViewById(R.id.rv_top5);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<EstudianteRanking> listaTop = new ArrayList<>();
        RankingAdapter adapter = new RankingAdapter(listaTop);
        rv.setAdapter(adapter);



        rankingController.cargarRanking(new RankingCallback() {
            @Override
            public void onRankingCargado(List<EstudianteRanking> ranking) {
                listaTop.clear();
                listaTop.addAll(ranking);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(TeacherActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ==================== ADAPTERS ====================
    static class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.VH> {
        private final List<EstudianteRanking> data;

        AlumnosAdapter(List<EstudianteRanking> d) {
            data = d;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_alumno, p, false));
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            EstudianteRanking a = data.get(pos);
            h.tvNombre.setText(a.nombre);
            h.tvDni.setText(String.format("%.0f%% • %d quizzes", a.porcentaje, a.quizzesRealizados));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvNombre, tvDni;

            VH(View v) {
                super(v);
                tvNombre = v.findViewById(R.id.tv_nombre_alumno);
                tvDni = v.findViewById(R.id.tv_dni);
            }
        }
    }

    static class TemasAdapter extends RecyclerView.Adapter<TemasAdapter.VH> {
        private final List<EstadisticasTema> data;

        TemasAdapter(List<EstadisticasTema> d) {
            data = d;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(android.R.layout.simple_list_item_2, p, false));
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            EstadisticasTema t = data.get(pos);
            h.t1.setText(t.tema);
            h.t1.setTypeface(null, Typeface.BOLD);
            h.t2.setText(t.promedioGeneral + "%");
            h.t2.setTextColor(0xFF006400);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView t1, t2;

            VH(View v) {
                super(v);
                t1 = v.findViewById(android.R.id.text1);
                t2 = v.findViewById(android.R.id.text2);
            }
        }
    }

    static class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.VH> {
        private final List<EstudianteRanking> data;

        RankingAdapter(List<EstudianteRanking> d) {
            data = d;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_ranking, p, false));
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            EstudianteRanking e = data.get(pos);
            h.tvIniciales.setText(e.iniciales);
            h.tvNombre.setText(e.nombre);
            h.tvPorcentaje.setText(String.format("%.0f%%", e.porcentaje));
            h.tvTemas.setText("📚 " + e.temasCompletados + " temas completados");

            // Set position with medals for top 3
            String posText;
            int color;
            switch (pos) {
                case 0:
                    posText = "🥇";
                    color = 0xFFFFD700;
                    break;
                case 1:
                    posText = "🥈";
                    color = 0xFFC0C0C0;
                    break;
                case 2:
                    posText = "🥉";
                    color = 0xFFCD7F32;
                    break;
                default:
                    posText = String.valueOf(pos + 1);
                    color = 0xFFFFFFFF;
            }
            h.tvPosicion.setText(posText);
            h.card.setCardBackgroundColor(color);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvIniciales, tvNombre, tvPorcentaje, tvTemas, tvPosicion;
            CardView card;

            VH(View v) {
                super(v);
                tvIniciales = v.findViewById(R.id.tv_iniciales);
                tvNombre = v.findViewById(R.id.tv_nombre_ranking);
                tvPorcentaje = v.findViewById(R.id.tv_porcentaje_ranking);
                tvTemas = v.findViewById(R.id.tv_temas_ranking);
                tvPosicion = v.findViewById(R.id.tv_posicion);
                card = v.findViewById(R.id.card_ranking);
            }
        }
    }
}
