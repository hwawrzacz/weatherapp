package com.wawrzacz.weather.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

object MyFragmentManager {
    lateinit var activity: FragmentActivity
    var fragmentContainer = 0

    fun replaceWithSubFragment(newFragment: Fragment) {
        if (!isFragmentAlreadyVisible(newFragment)){
            val newFragmentTag = newFragment::class.java
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

            fragmentTransaction
                .replace(this.fragmentContainer, newFragment, newFragmentTag.toString())
                .addToBackStack(newFragment.tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    fun replaceWithPrimaryFragment(fragment: Fragment) {
        closeFragment()

        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction
            .replace(fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun isFragmentAlreadyVisible(newFragment: Fragment): Boolean {
        val currentFragment = activity!!.supportFragmentManager.findFragmentById(fragmentContainer)
        var currentFragmentTag: String? = null
        val newFragmentTag = newFragment::class.java.toString()

        if (currentFragment != null) {
            currentFragmentTag = currentFragment!!::class.java.toString()
        }

        return newFragmentTag == (currentFragmentTag)
    }

    fun closeFragment() {
        activity!!.supportFragmentManager.popBackStack()
    }
}