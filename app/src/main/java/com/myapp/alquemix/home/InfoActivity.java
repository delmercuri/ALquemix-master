package com.myapp.alquemix.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.myapp.alquemix.R;
import com.myapp.alquemix.quiz.QuizActivity;

/**
 * Vista de Información de Tema - Módulo Home
 * Muestra contenido educativo completo para cada tema
 */
public class InfoActivity extends AppCompatActivity {

    private LinearLayout contentLayout;
    private int temaId;
    private String nombreTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        temaId = getIntent().getIntExtra("TEMA_ID", 1);
        nombreTema = getIntent().getStringExtra("TEMA_NOMBRE");

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.setFillViewport(true);

        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(48, 48, 48, 48);
        contentLayout.setBackgroundResource(R.drawable.color1);
        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        addTitle(nombreTema);
        loadContent();

        scrollView.addView(contentLayout);
        setContentView(scrollView);
    }

    private void loadContent() {
        switch (temaId) {
            case 1:
                loadIntroHidrocarburos();
                break;
            case 2:
                loadAlcanos();
                break;
            case 3:
                loadAlquenos();
                break;
            case 4:
                loadAlquinos();
                break;
            case 5:
                loadAlcoholes();
                break;
            case 6:
                loadReacciones();
                break;
        }
    }

    private void loadIntroHidrocarburos() {
        addImage(R.drawable.hc1);

        addSection("📚 Definición",
                "Los hidrocarburos son compuestos orgánicos fundamentales en la química, compuestos exclusivamente por átomos de carbono (C) e hidrógeno (H). No contienen otros elementos como oxígeno, nitrógeno o halógenos en su estructura básica. Representan la base de la química orgánica y son esenciales en la vida cotidiana, ya que forman parte de combustibles fósiles como el petróleo, el gas natural y el carbón.");

        addImage(R.drawable.hc2);

        addSection("🔬 Clasificación detallada",
                "• Hidrocarburos alifáticos: Incluyen cadenas abiertas (acíclicos) o cerradas (cíclicos, pero no aromáticos). \"Alifático\" proviene del griego \"aleiphar\" (grasa).\n\n"
                        +
                        "• Saturados: Solo enlaces simples entre carbonos, como los alcanos (fórmula general CₙH₂ₙ₊₂). Son estables y poco reactivos.\n\n"
                        +
                        "• Insaturados: Contienen enlaces dobles (alquenos, CₙH₂ₙ) o triples (alquinos, CₙH₂ₙ₋₂), lo que les confiere mayor reactividad.\n\n"
                        +
                        "• Hidrocarburos aromáticos: Caracterizados por anillos estables como el benceno (C₆H₆), con enlaces alternados que exhiben resonancia.");

        addImage(R.drawable.hc3);

        addSection("⚗️ Propiedades físicas",
                "• Son apolares, insolubles en agua pero solubles en solventes orgánicos como el éter o el benceno.\n\n"
                        +
                        "• Sus puntos de ebullición y fusión aumentan con el número de carbonos (masa molecular).\n\n" +
                        "• Hidrocarburos de cadena corta (hasta 4 C) son gases a temperatura ambiente, intermedios son líquidos, y largos son sólidos.\n\n"
                        +
                        "• El metano (CH₄) es el hidrocarburo más simple, principal componente del gas natural.");

        addSection("🌍 Importancia y usos",
                "Los hidrocarburos son cruciales en la industria petroquímica para producir plásticos, solventes, lubricantes y combustibles. Sin embargo, su combustión contribuye al cambio climático por emisiones de CO₂.");

        addStartQuizButton();
    }

    private void loadAlcanos() {
        addImage(R.drawable.alcano1);

        addSection("📚 Definición",
                "Los alcanos son hidrocarburos saturados con solo enlaces simples C-C y C-H, siguiendo la fórmula general CₙH₂ₙ₊₂. Cada átomo de carbono forma cuatro enlaces (hibridación sp³), con geometría tetraédrica y ángulos de 109.5°.");

        addImage(R.drawable.alcano2);

        addSection("📝 Nomenclatura IUPAC",
                "• El sufijo es \"-ano\". La cadena principal se elige como la más larga continua de carbonos.\n\n" +
                        "• Prefijos: met- (1C), et- (2C), prop- (3C), but- (4C), pent- (5C), hex- (6C).\n\n" +
                        "• Para ramificaciones: Se nombran como alquilos (ej. metil- para -CH₃) y se numeran desde el extremo que da los números más bajos.\n\n"
                        +
                        "• Ejemplos: Butano (C₄H₁₀), isopentano (2-metilbutano).");

        addImage(R.drawable.alcano3);

        addSection("⚗️ Propiedades físicas",
                "• Poco reactivos químicamente (conocidos como parafinas, del latín \"parum affinis\" = poca afinidad).\n\n"
                        +
                        "• Alta energía de disociación de enlaces C-C (348 kJ/mol) y C-H (413 kJ/mol).\n\n" +
                        "• Densidades bajas (0.6-0.8 g/cm³), flotan en agua.\n\n" +
                        "• Usos: Combustibles (gasolina, queroseno), lubricantes y ceras.");

        addSection("🔥 Reacciones clave",
                "• Combustión: Completa (con exceso O₂) produce CO₂ + H₂O; incompleta genera CO o hollín.\n\n" +
                        "• Halogenación: Reacción radicalaria iniciada por luz UV (CH₄ + Cl₂ → CH₃Cl + HCl).\n\n" +
                        "• Isomería: Estructural (cadena vs. ramificada) aumenta con más carbonos.");

        addStartQuizButton();
    }

    private void loadAlquenos() {
        addImage(R.drawable.alqueno1);

        addSection("📚 Definición",
                "Los alquenos son hidrocarburos insaturados con al menos un enlace doble carbono-carbono (C=C), con fórmula general CₙH₂ₙ. El enlace doble consiste en un enlace σ (fuerte) y un enlace π (débil, ~264 kJ/mol), haciéndolos más reactivos que los alcanos.");

        addImage(R.drawable.alqueno2);

        addSection("📝 Nomenclatura y estructura",
                "• Sufijo \"-eno\". La posición del doble enlace se indica con el número más bajo posible.\n\n" +
                        "• El eteno (C₂H₄), también llamado etileno, es el alqueno más simple, usado en la maduración de frutas y producción de plásticos.\n\n"
                        +
                        "• Geometría: Los carbonos del C=C son hibridados sp², con geometría trigonal plana (ángulos 120°).\n\n"
                        +
                        "• Permite isomería cis-trans (geométrica) en alquenos disustituidos.");

        addImage(R.drawable.alqueno3);

        addSection("🧪 Propiedades y pruebas",
                "• Más densos que alcanos equivalentes, pero aún apolares.\n\n" +
                        "• Prueba de insaturación: Decoloran agua de bromo (Br₂ en agua, de rojo-naranja a incoloro).\n\n"
                        +
                        "• También decoloran permanganato de potasio (KMnO₄, prueba de Baeyer) formando dioles.");

        addSection("⚡ Reacciones detalladas",
                "• Adición electrofílica: El doble enlace actúa como nucleófilo.\n\n" +
                        "• Hidrogenación: + H₂ (catalizador Pt/Ni) → alcano (usado en margarina).\n\n" +
                        "• Adición de HX: Sigue regla de Markovnikov (H al C con más H).\n\n" +
                        "• Polimerización: Formación de polietileno, polipropileno.\n\n" +
                        "• Usos: Materias primas para plásticos, solventes y detergentes.");

        addStartQuizButton();
    }

    private void loadAlquinos() {
        addImage(R.drawable.alquino1);

        addSection("📚 Definición",
                "Los alquinos contienen al menos un enlace triple carbono-carbono (C≡C), con fórmula general CₙH₂ₙ₋₂. El enlace triple incluye un σ y dos π (energía total ~839 kJ/mol, pero los π son accesibles para reacciones).");

        addImage(R.drawable.alquino2);

        addSection("📝 Nomenclatura y estructura",
                "• Sufijo \"-ino\". Posición del triple enlace con número bajo (ej. propino es CH₃-C≡CH).\n\n" +
                        "• El etino (C₂H₂), conocido como acetileno, es un gas usado en soldadura y síntesis orgánica.\n\n"
                        +
                        "• Geometría: Carbonos del C≡C hibridados sp, lineal (ángulos 180°).");

        addImage(R.drawable.alquino3);

        addSection("⚗️ Propiedades especiales",
                "• Alquinos terminales (con H en el triple, R-C≡C-H) son ácidos débiles (pKa ~25).\n\n" +
                        "• Prueba cualitativa: Reaccionan con amoniacal de cobre formando precipitados rojos de acetiluros.\n\n"
                        +
                        "• Con plata amoniacal forman precipitados blancos.");

        addSection("🔬 Reacciones clave",
                "• Adición: Pueden adicionar dos moléculas (ej. + 2 HX → gem-dihaluro).\n\n" +
                        "• Hidratación: Con HgSO₄/H₂SO₄, forma cetonas vía enoles (tautomería).\n\n" +
                        "• Hidrogenación parcial: Con catalizador Lindlar, da cis-alquenos.\n\n" +
                        "• Alquilación: Alquinos terminales desprotonados reaccionan con haluros.\n\n" +
                        "• Usos: Síntesis de vinilo y otros polímeros.");

        addStartQuizButton();
    }

    private void loadAlcoholes() {
        addImage(R.drawable.alcohol1);

        addSection("📚 Definición",
                "Los alcoholes son compuestos orgánicos con un grupo funcional hidroxilo (-OH) unido a un carbono saturado (sp³). No confundir con fenoles, donde -OH está en un anillo aromático. Fórmula general R-OH, donde R es alquilo.");

        addImage(R.drawable.alcohol2);

        addSection("🔬 Clasificación detallada",
                "• Primarios (1°): -OH unido a C con solo un alquilo (ej. etanol CH₃CH₂OH, usado en bebidas y desinfectantes).\n\n"
                        +
                        "• Secundarios (2°): -OH en C con dos alquilos (ej. isopropanol (CH₃)₂CHOH).\n\n" +
                        "• Terciarios (3°): -OH en C con tres alquilos (ej. tert-butanol).\n\n" +
                        "• Fenol (C₆H₅OH): -OH en anillo bencénico, más ácido (pKa ~10), usado en antisépticos.");

        addImage(R.drawable.alcohol3);

        addSection("📝 Nomenclatura",
                "• Sufijo \"-ol\", numerando para dar al -OH el número más bajo (ej. propan-1-ol).\n\n" +
                        "• Metanol (CH₃OH): El alcohol más simple, tóxico pero usado en combustibles y síntesis.");

        addSection("⚗️ Propiedades físicas y químicas",
                "• Polares debido al -OH, forman puentes de hidrógeno intermoleculares.\n\n" +
                        "• Puntos de ebullición elevados (ej. etanol 78°C vs. etano -89°C).\n\n" +
                        "• Solubles en agua los de cadena corta.\n\n" +
                        "• Ácidos débiles (pKa ~15-18), reaccionan con metales activos como Na.");

        addSection("🔥 Reacciones clave",
                "• Oxidación: 1° → aldehídos → ácidos; 2° → cetonas; 3° no se oxidan fácilmente.\n\n" +
                        "• Deshidratación: Con ácido concentrado y calor, forma alquenos (regla Zaitsev).\n\n" +
                        "• Esterificación: Con ácidos carboxílicos → ésteres (aromas, sabores).\n\n" +
                        "• Usos: Solventes, antisépticos, combustibles (etanol en gasolina).");

        addStartQuizButton();
    }

    private void loadReacciones() {
        addImage(R.drawable.reac1);

        addSection("📚 Definición básica",
                "Las reacciones químicas involucran la ruptura y formación de enlaces, con conservación de masa y energía. Se clasifican por tipo, mecanismo o cambio energético. El símbolo (aq) indica \"acuoso\", disuelto en agua.");

        addImage(R.drawable.reac2);

        addSection("🔬 Clasificación por tipo",
                "• Síntesis (combinación): Dos o más reactivos forman un producto (ej. 2H₂ + O₂ → 2H₂O).\n\n" +
                        "• Descomposición: Un compuesto se divide (ej. 2H₂O → 2H₂ + O₂, requiere energía).\n\n" +
                        "• Desplazamiento simple: Un elemento desplaza a otro (ej. Zn + CuSO₄ → ZnSO₄ + Cu).\n\n" +
                        "• Doble desplazamiento (metátesis): Intercambio de iones (ej. AgNO₃ + NaCl → AgCl↓ + NaNO₃).");

        addImage(R.drawable.reac3);

        addSection("⚡ Más tipos de reacciones",
                "• Neutralización: Ácido + base → sal + agua (ej. HCl + NaOH → NaCl + H₂O).\n\n" +
                        "• Combustión: Reacción con O₂, liberando calor y luz (ej. CH₄ + 2O₂ → CO₂ + 2H₂O).\n\n" +
                        "• Redox: Involucran transferencia de electrones (oxidación y reducción).");

        addSection("🌡️ Cambio energético",
                "• Exotérmicas: Liberan calor (ΔH < 0), como la combustión.\n\n" +
                        "• Endotérmicas: Absorben calor (ΔH > 0), como la fotosíntesis.");

        addSection("⚗️ Factores que influyen",
                "• Catalizador: Acelera la reacción sin consumirse, bajando la energía de activación.\n\n" +
                        "• Velocidad: Depende de concentración, temperatura, superficie y catalizadores.\n\n" +
                        "• En química orgánica: mecanismos electrofílico, nucleófilo, y radicalario.");

        addStartQuizButton();
    }

    private void addTitle(String text) {
        // Card para el título
        CardView cardTitle = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 32);
        cardTitle.setLayoutParams(cardParams);
        cardTitle.setRadius(48);
        cardTitle.setCardBackgroundColor(Color.parseColor("#33FFFFFF"));
        cardTitle.setCardElevation(0);
        cardTitle.setContentPadding(32, 24, 32, 24);

        TextView tv = new TextView(this);
        tv.setText("📖 " + text);
        tv.setTextSize(22);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);

        cardTitle.addView(tv);
        contentLayout.addView(cardTitle);
    }

    private void addSection(String title, String content) {
        // Card contenedor
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setRadius(32);
        card.setCardBackgroundColor(Color.parseColor("#20FFFFFF"));
        card.setCardElevation(0);
        card.setContentPadding(24, 20, 24, 20);

        LinearLayout sectionLayout = new LinearLayout(this);
        sectionLayout.setOrientation(LinearLayout.VERTICAL);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextSize(17);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextColor(Color.parseColor("#AAFFAA"));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(0, 0, 0, 12);
        tvTitle.setLayoutParams(titleParams);
        sectionLayout.addView(tvTitle);

        TextView tvContent = new TextView(this);
        tvContent.setText(content);
        tvContent.setTextSize(15);
        tvContent.setTextColor(Color.WHITE);
        tvContent.setLineSpacing(0, 1.3f);
        sectionLayout.addView(tvContent);

        card.addView(sectionLayout);
        contentLayout.addView(card);
    }

    private void addImage(int resId) {
        CardView cardImage = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 400);
        cardParams.setMargins(0, 16, 0, 24);
        cardImage.setLayoutParams(cardParams);
        cardImage.setRadius(32);
        cardImage.setCardElevation(12);

        ImageView iv = new ImageView(this);
        iv.setImageResource(resId);
        iv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        cardImage.addView(iv);
        contentLayout.addView(cardImage);
    }

    private void addStartQuizButton() {
        com.google.android.material.button.MaterialButton btn = new com.google.android.material.button.MaterialButton(
                this);
        btn.setText("🎯 INICIAR QUIZ");
        btn.setTextSize(16);
        btn.setTextColor(Color.parseColor("#052450"));
        btn.setTypeface(null, Typeface.BOLD);
        btn.setBackgroundColor(Color.parseColor("#AAFFAA"));
        btn.setCornerRadius(60);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 140);
        params.setMargins(0, 48, 0, 64);
        btn.setLayoutParams(params);

        btn.setOnClickListener(v -> {
            Intent i = new Intent(this, QuizActivity.class);
            i.putExtra("TEMA_ID", temaId);
            i.putExtra("TEMA_NOMBRE", nombreTema);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        contentLayout.addView(btn);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
