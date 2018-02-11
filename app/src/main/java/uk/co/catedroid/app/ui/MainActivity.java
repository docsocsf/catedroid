package uk.co.catedroid.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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

    private static final String STATE_CURRENT_FRAGMENT = "catedroid.main.currentfragment";
    private static final int KEY_FRAGMENT_HOME = 0;
    private static final int KEY_FRAGMENT_TIMETABLE = 1;
    private static final int KEY_FRAGMENT_NOTES = 2;

    private int currentFragment = -1;

    @BindView(R.id.main_bottom_navigation) protected BottomNavigationView bottomNavigationMenu;

    @Inject
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((CATeApplication) getApplication()).getNetComponent().inject(this);

        if (savedInstanceState != null) {
            switchFragment(savedInstanceState.getInt(STATE_CURRENT_FRAGMENT, KEY_FRAGMENT_HOME), true);
        } else {
            switchFragment(KEY_FRAGMENT_HOME, true);
        }

        bottomNavigationMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch(item.getItemId()) {
                            case R.id.main_nav_home:
                                Log.d("CATe", "Home");
                                switchFragment(KEY_FRAGMENT_HOME, false);
                                break;
                            case R.id.main_nav_timetable:
                                Log.d("CATe", "Timetable");
                                break;
                            case R.id.main_nav_notes:
                                Log.d("CATe", "Notes");
                                switchFragment(KEY_FRAGMENT_NOTES, false);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void switchFragment(int target, boolean override) {
        if (target == currentFragment && !override) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment toReplace;
        switch (target) {
            case KEY_FRAGMENT_HOME:
                toReplace = new DashboardFragment();
                break;
            case KEY_FRAGMENT_NOTES:
                toReplace = new NotesListFragment();
                break;
            default:
                toReplace = new DashboardFragment();
        }

        transaction.replace(R.id.main_fragment_container, toReplace);
        transaction.commit();

        currentFragment = target;
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
