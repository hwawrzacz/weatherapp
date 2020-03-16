package com.wawrzacz.weather.view.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import com.wawrzacz.weather.utils.Validator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wawrzacz.weather.R
import com.wawrzacz.weather.data.model.WeatherDataResponse
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

        initializeViewModel()
        unsetActionBar()

        // Initialize fields from view
        searchButton = view.search_button
        cityNameInputLayout = view.input_layout
        cityNameInput = view.city_name_input

        // Add listeners
        searchButton.setOnClickListener {
            this.onSearch()
        }

        // Find by location
        cityNameInputLayout.setEndIconOnClickListener {
            // TODO: Handle search by location
            cleanError()

            if (isLocationPermissionGranted()) {
                // Get location
                // Make and handle API call based on location
            } else {
                // Try to get location permission and possibly make API call
                handleNoLocationPermission()
            }
        }

        return view
    }

    private fun initializeViewModel() {
        val viewModelFactory = WeatherViewModelFactory()
        viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(WeatherViewModel::class.java)
    }

    private fun unsetActionBar() {
        val compatActivity = activity as AppCompatActivity
        val supportActionBar = compatActivity.supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun onSearch() {
        val enteredText = cityNameInput.text.toString()

        if (Validator.isValid(enteredText)) {
            if (hasInternetAccess()) {
                enableLoadingAnimation()
                hideKeyboard(searchButton)
                makeAndHandleApiCall()
            } else {
                handleNoInternetAccess()
            }
        }
        handleInvalidInput(enteredText)
    }

    private fun hasInternetAccess(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork

        return network !== null
    }
    
    private fun enableLoadingAnimation() {
        this.searchButton.text = getString(R.string.btn_checking)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun makeAndHandleApiCall() {
        viewModel.getWeatherData(this.cityNameInput.text.toString().trim())
            .observe(this.viewLifecycleOwner, Observer {
                handleApiResponse(it)
            })
    }

    private fun handleApiResponse(response: WeatherDataResponse?) {
        if (response !== null) {
            val weatherData = response.weatherData
            Log.i("schab", "Changed ${weatherData.toString()}")
            viewModel.weatherData.value = weatherData

            if (response.isLoading) {
                enableLoadingAnimation()
            } else if (response.isFinished && response.isSuccess) {
                openDetailsFragment()
                disableLoadingAnimation()
            } else if (response.isFinished && !response.isSuccess) {
                makeSnackBarLong(getString(R.string.err_city_not_found), getString(R.string.common_ok))
                disableLoadingAnimation()
            }
        }
    }

    private fun openDetailsFragment() {
        val detailsFragment = DetailsFragment()
        MyFragmentManager.replaceWithSubFragment(detailsFragment)
    }

    private fun disableLoadingAnimation() {
        this.searchButton.text = getString(R.string.btn_check)
    }

    private fun handleNoInternetAccess() {
        makeSnackBarLong(getString(R.string.err_no_internet_access), getString(R.string.common_ok))
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun handleNoLocationPermission() {
        makeSnackBarLong("Nie przyznano uprawnień do lokalizacji", getString(R.string.common_ok))
    }

    private fun setError(errorMessage: String) {
        cityNameInputLayout.error = errorMessage
        cityNameInputLayout.errorIconDrawable = null
    }

    private fun cleanError() {
        cityNameInputLayout.error = null
    }

    private fun handleInvalidInput(text: String) {
        when {
            !Validator.isValidNotEmpty(text) -> {
                setError(getString(R.string.err_input_cannot_be_empty))
            }
            !Validator.isValidNoNumber(text) -> {
                setError(getString(R.string.err_input_cannot_contain_numbers))
            }
            !Validator.isValidNoSpecialCharacters(text) -> {
                setError(getString(R.string.err_input_cannot_contain_special_characters))
            }
            else -> cleanError()
        }
    }

    private fun makeSnackBarLong(message: String, actionText: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
            .setAction(actionText, {})
            .show()
    }
}
