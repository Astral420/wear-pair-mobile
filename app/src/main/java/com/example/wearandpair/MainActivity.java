package com.example.wearandpair;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements Home.OnHomeNavigationListener {

    private BottomNavigationView bottomNavigationView;
    private boolean isNavigatingFromHome = false;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // --- NEW CODE: Add the Back Stack Listener ---
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // This method will be called every time the back stack changes (e.g., after pressing back)
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof Home) {
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                } else if (currentFragment instanceof ProductsFragment) {
                    bottomNavigationView.setSelectedItemId(R.id.navigation_products);
                } else if (currentFragment instanceof OutfitBuilder) {
                    bottomNavigationView.setSelectedItemId(R.id.navigation_builder);
                } else if (currentFragment instanceof UserProfile) {
                    bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
                }
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment selected_fragment = null;
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                // --- MODIFIED LOGIC: Prevent reloading the same fragment ---
                if (itemId == R.id.navigation_home) {
                    if (!(currentFragment instanceof Home)) {
                        selected_fragment = new Home();
                    }
                } else if (itemId == R.id.navigation_products) {
                    if (isNavigatingFromHome) {
                        isNavigatingFromHome = false;
                        return true;
                    }
                    if (!(currentFragment instanceof ProductsFragment)) {
                        selected_fragment = new ProductsFragment();
                    }
                } else if (itemId == R.id.navigation_builder) {
                    if (!(currentFragment instanceof OutfitBuilder)) {
                        selected_fragment = new OutfitBuilder();
                    }
                } else if (itemId == R.id.navigation_profile) {
                    if (!(currentFragment instanceof UserProfile)) {
                        selected_fragment = new UserProfile();
                    }
                }

                if (selected_fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selected_fragment)

                            .commit();
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Home())
                    .commit();
        }
    }

    @Override
    public void onNavigateToProducts() {
        isNavigatingFromHome = true;
        bottomNavigationView.setSelectedItemId(R.id.navigation_products);
    }
}