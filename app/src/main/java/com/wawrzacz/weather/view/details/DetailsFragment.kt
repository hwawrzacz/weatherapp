package com.wawrzacz.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import com.wawrzacz.weather.R
import com.wawrzacz.weather.navigation.MyFragmentManager
import kotlinx.android.synthetic.main.fragment_details.view.*

class DetailsFragment: Fragment() {
    private val myFragmentManager = MyFragmentManager
    lateinit var lastUpdateTime: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        val infoButton = view.info_button
        val backButton = view.back_button

        lastUpdateTime = "20.01.2020"
        infoButton.setOnClickListener {
            TooltipCompat.setTooltipText(it, "Ostatnia aktualiacja: $lastUpdateTime")
        }

        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }
}