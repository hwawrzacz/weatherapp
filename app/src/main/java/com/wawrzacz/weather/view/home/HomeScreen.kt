package com.wawrzacz.weather.view.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import com.wawrzacz.weather.utils.Validator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wawrzacz.weather.R
import com.wawrzacz.weather.data.model.WeatherDataResponse
import com.wawrzacz.weather.viewmodel.WeatherViewModel
import com.wawrzacz.weather.viewmodel.WeatherViewModelFactory
import kotlinx.android.synthetic.main.fragment_home_page.view.*

class HomeScreen: Fragment() {
    private lateinit var searchButton: MaterialButton
    private lateinit var cityNameInputLayout: TextInputLayout
    private lateinit var cityNameInput: TextInputEditText
    private lateinit var viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_ACCESS_REQUEST_CODE = 99

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        initializeViewModel()
        disableUpButton()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        // Initialize fields from view
        searchButton = view.search_button
        cityNameInputLayout = view.input_layout
        cityNameInput = view.city_name_input

        // Add listeners
        searchButton.setOnClickListener {
            onSearchByName()
        }

        // Find by location
        cityNameInputLayout.setEndIconOnClickListener {
            cleanError()
            onSearchByLocation()
        }

        return view
    }

    private fun initializeViewModel() {
        val viewModelFactory = WeatherViewModelFactory()
        viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(WeatherViewModel::class.java)
    }

    private fun disableUpButton() {
        val compatActivity = activity as AppCompatActivity
        val supportActionBar = compatActivity.supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun onSearchByName() {
        val enteredText = cityNameInput.text.toString()

        if (Validator.isValid(enteredText)) {
            if (hasInternetAccess()) {
                enableLoadingAnimation()
                hideKeyboard(searchButton)
                makeAndHandleApiCallByName()
            } else {
                handleNoInternetAccess()
            }
        }
        handleInvalidInput(enteredText)
    }

    private fun onSearchByLocation() {
        if (isLocationPermissionGranted()) {
            handleFineLocationPermissionGranted()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_ACCESS_REQUEST_CODE)
        }
    }

    private fun makeAndHandleApiCallByName() {
        viewModel.getWeatherDataByName(this.cityNameInput.text.toString().trim())
            .observe(this.viewLifecycleOwner, Observer {
                handleApiResponse(it)
            })
    }

    private fun makeAndHandleApiCallByLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location === null){
                makeSnackBarLong(getString(R.string.location_is_disabled), "")
            } else {
                viewModel.getWeatherDataByLocation(location)
                    .observe(this.viewLifecycleOwner, Observer { weatherDateResponse: WeatherDataResponse ->
                        handleApiResponse(weatherDateResponse)
                    })
            }
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_ACCESS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleFineLocationPermissionGranted()
                } else if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    handleCoarseLocationPermissionGranted()
                } else {
                    handleNoLocationPermission()
                }
            }
            else -> handleNoLocationPermission()
        }
    }

    private fun handleFineLocationPermissionGranted() {
        makeAndHandleApiCallByLocation()
    }

    private fun handleCoarseLocationPermissionGranted() {
        makeSnackBarLong(getString(R.string.alert_location_low_accuracy), getString(R.string.common_ok))
        makeAndHandleApiCallByLocation()
    }

    private fun openDetailsFragment() {
        view?.findNavController()?.navigate(R.id.action_homeScreen_to_detailsFragment)
    }

    private fun hasInternetAccess(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork

        return network !== null
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun enableLoadingAnimation() {
        this.searchButton.text = getString(R.string.btn_checking)
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
        makeSnackBarLong(getString(R.string.no_location_permission), getString(R.string.common_ok))
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

    private fun makeSnackBarLong(message: String, actionText: String?) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
            .setAction(actionText, {})
            .show()
    }
}
