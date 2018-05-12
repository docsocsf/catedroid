package uk.co.catedroid.app.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import javax.inject.Inject

import kotlinx.android.synthetic.main.activity_main.*

import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.R
import uk.co.catedroid.app.auth.LoginManager

class MainActivity : AppCompatActivity() {

    private var currentFragment = -1

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as CATeApplication).netComponent.inject(this)

        if (savedInstanceState != null) {
            switchFragment(savedInstanceState.getInt(STATE_CURRENT_FRAGMENT, KEY_FRAGMENT_HOME),
                    true)
        } else {
            switchFragment(KEY_FRAGMENT_HOME, true)
        }

        main_bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_nav_home -> {
                    Log.d("CATe", "Home")
                    switchFragment(KEY_FRAGMENT_HOME, false)
                }
                R.id.main_nav_timetable -> Log.d("CATe", "Timetable")
                R.id.main_nav_notes -> {
                    Log.d("CATe", "Notes")
                    switchFragment(KEY_FRAGMENT_NOTES, false)
                }
            }
            true
        }
    }

    private fun switchFragment(target: Int, override: Boolean) {
        if (target == currentFragment && !override) {
            return
        }

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val toReplace: Fragment = when (target) {
            KEY_FRAGMENT_HOME -> DashboardFragment()
            KEY_FRAGMENT_NOTES -> NotesListFragment()
            else -> DashboardFragment()
        }

        transaction.replace(R.id.main_fragment_container, toReplace)
        transaction.commit()

        currentFragment = target
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_logout -> {
                val loginManager = LoginManager(sharedPreferences)
                loginManager.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val STATE_CURRENT_FRAGMENT = "catedroid.main.currentfragment"
        private const val KEY_FRAGMENT_HOME = 0
        private const val KEY_FRAGMENT_TIMETABLE = 1
        private const val KEY_FRAGMENT_NOTES = 2
    }


}
