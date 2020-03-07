package com.wawrzacz.weather.view.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wawrzacz.weather.R
import com.wawrzacz.weather.navigation.MyFragmentManager
import com.wawrzacz.weather.view.details.DetailsFragment
import com.wawrzacz.weather.viewmodel.WeatherViewModel
import com.wawrzacz.weather.viewmodel.WeatherViewModelFactory
import kotlinx.android.synthetic.main.fragment_home_page.view.*

class HomeScreen: Fragment() {
    private lateinit var searchButton: MaterialButton
    private lateinit var cityNameInputLayout: TextInputLayout
    private lateinit var cityNameInput: TextInputEditText
    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        // Initialize ViewModel
        val viewModelFactory = WeatherViewModelFactory()
        viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(WeatherViewModel::class.java)

        // Initialize fields from view
        searchButton = view.search_button
        cityNameInputLayout = view.input_layout
        cityNameInput = view.city_name_input

        // Add listeners
        searchButton.setOnClickListener {
            Log.i("schab","value changed")
            viewModel.cityName.value = cityNameInput.text.toString()
            this.onSearch()
        }

        // Check permission


        if (isLocationPermissionGranted()) {
            makeToastLong("Uprawnienia do lokalizacji przyznane")
        }
        else {
            makeToastLong("Uprawnienia do lokalizacji nie przyznane")
        }

        return view
    }

    private fun onSearch() {
        // ValidateInput
        if (isInputValid()) {
            cleanError()
            enableLoadingAnimation()
            hideKeyboard(searchButton)

            // Make API call

            // If API answer is correct, then open fragment with weather details
            val detailsFragment = DetailsFragment()
            MyFragmentManager.replaceWithSubFragment(detailsFragment)
        }
        else {
            showError("Pole nie może być puste")
//            showError(R.string.error_input_cannot_be_empty)
        }

    }

    private fun isInputValid(): Boolean {
        return !cityNameInput.text.isNullOrBlank()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun showError(errorMessage: String) {
        cityNameInputLayout.error = errorMessage
    }

    private fun cleanError() {
        cityNameInputLayout.error = null
    }

    private fun enableLoadingAnimation() {
        // TODO: Set loading icon on button
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun makeToastLong(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
