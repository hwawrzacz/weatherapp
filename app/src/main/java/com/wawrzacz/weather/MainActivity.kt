package com.wawrzacz.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        this.navController = findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, this.navController)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
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
}
