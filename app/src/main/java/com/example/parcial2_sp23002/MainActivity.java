package com.example.parcial2_sp23002;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.parcial2_sp23002.Fragment.IngredientFragment;
import com.example.parcial2_sp23002.Fragment.RecipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new RecipeFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            
            if (id == R.id.nav_recipes) {
                selectedFragment = new RecipeFragment();
            } else if (id == R.id.nav_ingredients) {
                selectedFragment = new IngredientFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}
