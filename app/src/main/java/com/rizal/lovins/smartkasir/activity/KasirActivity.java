package com.rizal.lovins.smartkasir.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.adapter.GroceriesDataAdapter;
import com.rizal.lovins.smartkasir.adapter.ProductDataAdapter;
import com.rizal.lovins.smartkasir.fragment.GoogleMapsFragment;
import com.rizal.lovins.smartkasir.fragment.HomeKasirFragment;
import com.rizal.lovins.smartkasir.fragment.ShopFragment;
import com.rizal.lovins.smartkasir.model.Product;
import com.rizal.lovins.smartkasir.util.PreferenceUtil;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class KasirActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    public static ProductDataAdapter dataProduk;
    public static GroceriesDataAdapter dataBalanjaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir);
        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_kasir);
        navigationView = (NavigationView) findViewById(R.id.naViewKasir);
        setupDrawer(navigationView);
        actionBarDrawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportFragmentManager().beginTransaction().replace(R.id.kontenKasir, new HomeKasirFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        setTitle(R.string.drawer_menu_smart_kasir);
        reqPerms();
        dataProduk = new ProductDataAdapter(this, Product.getInit(this));
        dataBalanjaan = new GroceriesDataAdapter(this);
    }

    private void reqPerms() {
        PermissionGen.with(KasirActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    private void setupDrawer(NavigationView nview) {
        nview.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
                        navigationDrawerMenu(menu);
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_buka, R.string.drawer_tutup);
    }

    private void navigationDrawerMenu(MenuItem menu) {
        Class fragclass;
        Fragment frag = null;
        switch (menu.getItemId()) {
            case R.id.frag1:
                fragclass = HomeKasirFragment.class;
                break;
            case R.id.frag2:
                fragclass = ShopFragment.class;
                break;
            case R.id.frag4:
                fragclass = GoogleMapsFragment.class;
                break;
            default:
                fragclass = HomeKasirFragment.class;
        }
        try {
            frag = (Fragment) fragclass.newInstance();
        } catch (Exception ignored) {
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.kontenKasir, frag).commit();
        menu.setChecked(true);
        setTitle(menu.getTitle());
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                menuSettings();
                return true;
            case R.id.menu_logout:
                menuLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void menuSettings() {
        startActivity(new Intent(KasirActivity.this, SettingsActivity.class));
    }

    private void menuLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(KasirActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Apakah Anda yakin ingin keluar?");
        builder.setCancelable(true);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PreferenceUtil.logout(KasirActivity.this);
                startActivity(new Intent(KasirActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        // Lakukan sesuatu disini
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Perijinan ditolak");
        builder.setCancelable(false);
        builder.setMessage("Untuk menggunakan Aplikasi ini kamu perlu membolehkan beberapa perijinan yang diajukan. Atau Aplikasi ini tidak bisa digunakan");
        builder.setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface p1, int p2) {
                KasirActivity.this.finish();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        GroceriesDataAdapter.totalBelanja = 0;
        super.onDestroy();
    }

}
