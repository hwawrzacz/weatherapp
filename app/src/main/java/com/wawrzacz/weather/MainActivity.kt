package com.wawrzacz.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.wawrzacz.weather.navigation.MyFragmentManager
import com.wawrzacz.weather.view.home.HomeScreen

class MainActivity : AppCompatActivity() {
    private val myFragmentManager = MyFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        myFragmentManager.activity = this
        myFragmentManager.fragmentContainer = R.id.fragment_container

        if (savedInstanceState === null) {
            val homeScreenFragment = HomeScreen()
            myFragmentManager.replaceWithPrimaryFragment(homeScreenFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.menu_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.info_button -> {
                val alertDialog = createInfoAlertDialog()
                alertDialog?.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createInfoAlertDialog(): AlertDialog? {
        val alertDialogBuilder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }

        alertDialogBuilder?.setTitle(R.string.title_credits)
            ?.setMessage(R.string.content_credits)

        alertDialogBuilder?.setIcon(R.drawable.outline_info_24)
        alertDialogBuilder?.setPositiveButton(R.string.common_ok, null)

        return alertDialogBuilder?.create()
    }

    override fun onSupportNavigateUp(): Boolean {
        // TODO: Handle navigation bar change
        //      if home screen is opened, hide back and info button
        onBackPressed()
        return false
    }
}
