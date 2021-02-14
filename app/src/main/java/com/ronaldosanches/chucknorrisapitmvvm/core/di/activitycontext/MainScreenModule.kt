package com.ronaldosanches.chucknorrisapitmvvm.core.di.activitycontext

import android.app.Activity
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.callbacks.IFragActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class MainScreenModule {

    @Provides
    fun providesIFragActivityCallback(activity: Activity) = activity as IFragActivity
}