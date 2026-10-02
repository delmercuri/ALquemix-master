package com.myapp.alquemix.teacher;

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
import com.myapp.alquemix.teacher.RankingController.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Vista de Ranking Top - Módulo de Docente
 */
public class TopActivity extends AppCompatActivity {

    private RecyclerView rvRanking;
    private ImageButton btnBack;

    private RankingController rankingController;
    private TopAdapter adapter;
    private List<EstudianteRanking> listaRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        rankingController = RankingController.getInstance();
        listaRanking = new ArrayList<>();

        initViews();
        cargarRanking();
    }

    private void initViews() {
        rvRanking = findViewById(R.id.rv_ranking);
        rvRanking.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TopAdapter(listaRanking);
        rvRanking.setAdapter(adapter);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void cargarRanking() {
        rankingController.cargarRanking(new RankingCallback() {
            @Override
            public void onRankingCargado(List<EstudianteRanking> ranking) {
                listaRanking.clear();
                listaRanking.addAll(ranking);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(TopActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class TopAdapter extends RecyclerView.Adapter<TopAdapter.VH> {
        private final List<EstudianteRanking> data;

        TopAdapter(List<EstudianteRanking> d) {
            data = d;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            EstudianteRanking e = data.get(position);
            holder.tvIniciales.setText(e.iniciales);
            holder.tvNombre.setText(e.nombre);
            holder.tvPorcentaje.setText(String.format("%.0f%%", e.porcentaje));
            holder.tvTemas.setText("📚 " + e.temasCompletados + " temas completados");

            // Set position number with medals for top 3
            String posText;
            int color;
            switch (position) {
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
                    posText = String.valueOf(position + 1);
                    color = 0xFFFFFFFF;
            }
            holder.tvPosicion.setText(posText);
            holder.card.setCardBackgroundColor(color);
            holder.card.setCardElevation(position <= 2 ? 20 : 10);
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
