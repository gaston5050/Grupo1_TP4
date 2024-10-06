package com.example.grupo1_tp4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.grupo1_tp4.conexion.DataCategoriaMainActivity;
import com.example.grupo1_tp4.entidad.Categoria;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import com.example.grupo1_tp4.fragmentoAlta;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        AdaptadorViewPager adaptadorViewPager = new AdaptadorViewPager(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adaptadorViewPager.AgregarFragmento(new fragmentoAlta(), "Alta");
        adaptadorViewPager.AgregarFragmento(new fragmentoModificar(), "Modificar");
        adaptadorViewPager.AgregarFragmento(new fragmentoListar(), "Listar");
        viewPager.setAdapter(adaptadorViewPager);

        this.cargarCategorias(); //added by jp
    }


    public void cargarCategorias() {
        DataCategoriaMainActivity dataCategoriaMainActivity = new DataCategoriaMainActivity(null, this);
        dataCategoriaMainActivity.obtenerTodos(new DataCategoriaMainActivity.CategoriaCallback() {
            @Override
            public void onCategoriasObtenidas(List<Categoria> categorias) {
                // Definir la posición del fragmento a obtener
                int position = 0;

                // Obtener el fragmento por posición
                fragmentoAlta fragment = (fragmentoAlta) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);

                // Verificar si el fragmento es no nulo antes de llamar a cargarSpinner
                if (fragment != null) {
                    fragment.cargarSpinner(categorias); // Llama a cargarSpinner en el fragmento
                } else {
                    Log.e("FragmentError", "El fragmento no es del tipo esperado o es nulo");
                }
            }


            @Override
            public void onError(String mensaje) {
                // Manejo de errores
                Toast.makeText(MainActivity.this, "Error al obtener categorías: " + mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

}