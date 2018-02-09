package uk.co.catedroid.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.auth.LoginManager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_bottom_navigation) protected BottomNavigationView bottomNavigationMenu;

    @Inject
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((CATeApplication) getApplication()).getNetComponent().inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        DashboardFragment dashboardFragment = new DashboardFragment();
        transaction.add(R.id.main_fragment_container, dashboardFragment);
        transaction.commit();

        bottomNavigationMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.main_nav_home:
                                Log.d("CATe", "Home");
                                break;
                            case R.id.main_nav_timetable:
                                Log.d("CATe", "Timetable");
                                break;
                            case R.id.main_nav_notes:
                                Log.d("CATe", "Notes");
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_logout:
                LoginManager loginManager = new LoginManager(sharedPreferences);
                loginManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
