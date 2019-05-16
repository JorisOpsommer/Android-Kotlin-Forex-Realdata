package com.joris.clientapp

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.joris.clientapp.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val idlingResource = CountingIdlingResource("MAIN_LOADER")

    fun getIdlingResourceInTest(): CountingIdlingResource {
        return idlingResource
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
        navigation.setOnNavigationItemSelectedListener(bottomNavigationListener)
        val bundle = Bundle()
        changeFragment(HomeFragment(), bundle, "home", true)

    }

    /**
     * On reload, check if there was already an existing fragment & if yes, load that fragment
     */
    override fun onStart() {
        super.onStart()

        if (supportFragmentManager.backStackEntryCount != 1) {
            val bundle = Bundle()
            when (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2).name) {
                "home" -> changeFragment(HomeFragment(), bundle, "home", true)
                "search" -> changeFragment(SearchStockFragment(), bundle, "search", true)
                "history" -> changeFragment(HistoryFragment(), bundle, "history", true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Method to change fragment
     *@param bundle data that is needed in the fragment
     *@param tag give target fragment the correct tag. OnStart method now knows which fragment is current fragment
     */
    fun changeFragment(fragment: Fragment, bundle: Bundle, tag: String, addToStack: Boolean) {
        val fragmentManager = supportFragmentManager
        fragment.arguments = bundle
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (addToStack) {
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Links bottom navbar buttons to changefragment method
     */
    private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_home -> {
                val bundle = Bundle()
                changeFragment(HomeFragment(), bundle, "home", true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                val bundle = Bundle()
                changeFragment(SearchStockFragment(), bundle, "search", true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                val bundle = Bundle()
                changeFragment(HistoryFragment(), bundle, "history", true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


}
