package com.example.ecom.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ecom.IOnBackPressed;
import com.example.ecom.R;
import com.example.ecom.databinding.ActivityMainBinding;
import com.example.ecom.view.fragments.ProfileFragment;
import com.example.ecom.view.fragments.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Fragment selectedFragment = null;

    //Badge feature
    MenuItem menuItem;
    TextView badgeCount;
    int cartItemsCount = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ShopFragment()).commit();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menuItem = menu.findItem(R.id.cartIcon);

        if (cartItemsCount == 0) {
            menuItem.setActionView(null);
        } else {

            menuItem.setActionView(R.layout.cart_notification_badge_layout);
            View view = menuItem.getActionView();
            badgeCount = view.findViewById(R.id.badge_counter);
            badgeCount.setText(String.valueOf(cartItemsCount));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cartIcon) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}