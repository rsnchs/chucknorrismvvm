package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.activities

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ronaldosanches.chucknorrisapitmvvm.databinding.ActivityMainScreenBinding
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseActivity
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.callbacks.IFragActivity
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.ShowErrorAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class MainScreenActivity : BaseActivity(), IFragActivity {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var navController : NavController


    private val errorAlert = ShowErrorAlert(::openAlert, ::closeAlert, ::updateText)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: intent.getBundleExtra(ACTIVITY_STATE_SAVED))
        changeToSelectedTheme()
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupView() {
        navController = Navigation.findNavController(this, binding.navHostFragment.id)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleOnBackPressedForActivity(navController)
            }
        })
        errorAlert.collect(lifecycleScope)
    }

    private fun updateText(text: String) = runBlocking {
        binding.tvMainAlert.text = text
    }

    private fun changeToSelectedTheme() {
        val selectedMode = getCurrentTheme()
        AppCompatDelegate.setDefaultNightMode(selectedMode)
    }

    override fun rotateNightTheme() {
        switchToNextTheme()
    }

    override fun showAlert(message: String) {
        errorAlert.addNewMessage(message)
    }

    private fun closeAlert() {
        binding.grMainAlert.visibility = View.GONE
//        mIdlingResource?.setIdleState(false)
    }

    private fun openAlert() {
//        mIdlingResource?.setIdleState(true)
        binding.grMainAlert.visibility = View.VISIBLE
    }
}
