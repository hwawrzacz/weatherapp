package com.wawrzacz.weather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wawrzacz.weather.navigation.MyFragmentManager
import com.wawrzacz.weather.view.home.HomeScreen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val myFragmentManager = MyFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myFragmentManager.activity = this
        myFragmentManager.fragmentContainer = R.id.fragment_container

        val homeScreenFragment = HomeScreen()
        myFragmentManager.replaceWithPrimaryFragment(homeScreenFragment)
    }
}
