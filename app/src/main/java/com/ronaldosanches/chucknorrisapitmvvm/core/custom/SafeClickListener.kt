package com.ronaldosanches.chucknorrisapitmvvm.core.custom

import android.os.SystemClock
import android.view.View
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants

class SafeClickListener (
        private val interval: Int = Constants.AppConstraints.MIN_CLICK_INTERVAL,
        private val onSafeClick: (View) -> Unit
) : View.OnClickListener {

    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}