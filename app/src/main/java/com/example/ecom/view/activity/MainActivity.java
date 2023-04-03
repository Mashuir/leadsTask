package com.example.ecom.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.example.ecom.R;
import com.example.ecom.databinding.ActivityMainBinding;
import com.example.ecom.view.fragments.ProfileFragment;
import com.example.ecom.view.fragments.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ShopFragment()).commit();

        //bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_shop);
        ((BottomNavigationView) findViewById(R.id.bottomNavigation)).setSelectedItemId(R.id.navigation_shop);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_shop) {
                selectedFragment = new ShopFragment();
                item.setIcon(R.drawable.ic_baseline_home_24);
            }else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
                item.setIcon(R.drawable.ic_baseline_person_24);
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();

            return true;
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}