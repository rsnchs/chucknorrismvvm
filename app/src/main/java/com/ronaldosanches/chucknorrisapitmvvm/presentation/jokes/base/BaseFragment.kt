package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.callbacks.IFragActivity
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.HandleError

abstract class BaseFragment : Fragment() {

    private val fragCallback : IFragActivity by lazy { requireActivity() as IFragActivity }
    protected val handleError by lazy { HandleError(fragCallback) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    abstract fun setupView()

    protected fun rotateNightTheme(action : () -> Unit) {
        fragCallback.rotateNightTheme()
        action()
    }

    @Suppress("SameParameterValue")
    protected fun safeNavigate(@IdRes currentId: Int, directions: NavDirections) {
        if(currentId == this.findNavController().currentDestination?.id) {
            this.findNavController().navigate(directions)
        }
    }
}