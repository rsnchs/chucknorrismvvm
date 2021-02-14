package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.activities.ActivityViewModel

abstract class BaseActivity : AppCompatActivity() {

    val viewModel : ActivityViewModel by viewModels()

    companion object {
        const val ACTIVITY_STATE_SAVED = "saved_instance_state_key_activity"
    }

    abstract fun setupView()

    override fun onStart() {
        super.onStart()
        setupView()
    }

    private val prefs: SharedPreferences by lazy {
        this.getSharedPreferences(getString(R.string.shared_prefs_file_key), Context.MODE_PRIVATE)
    }

    protected fun getCurrentTheme() : Int {
        return prefs.getInt(getString(R.string.key_pref_night_mode), Constants.NightMode.LIGHT_MODE)
    }

    private fun restartActivity() {
        val bundle = Bundle()
        onSaveInstanceState(bundle)
        Intent(this, javaClass).apply {
            putExtra(ACTIVITY_STATE_SAVED, bundle)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    protected fun handleOnBackPressedForActivity(navController: NavController?) {
        val popBack = navController?.popBackStack()
        viewModel.handleBackPressed(popBack).observe(this) {
            finishActivity()
        }
    }

    private fun finishActivity() {
        finish()
    }

    protected fun switchToNextTheme() {
        val currentTheme = getCurrentTheme()
        viewModel.getCurrentNextTheme(currentTheme).observe(this, {
            changeCurrentThemeAndRestart(it)
        })
    }

    private fun changeCurrentThemeAndRestart(newTheme: Int) {
        prefs.edit().apply {
            putInt(getString(R.string.key_pref_night_mode),newTheme)
            apply()
        }
        restartActivity()
    }
}