package com.ronaldosanches.chucknorrisapitmvvm.core.di.fragmentcontext

import androidx.fragment.app.Fragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.ContextRetriever
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.DynamicListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class MainFragmentModule {

    @FragmentScoped
    @Provides
    fun providesContextRetriever(fragment: Fragment) = fragment as ContextRetriever

    @FragmentScoped
    @Provides
    fun providesDynamicListAdapter(contextRetriever: ContextRetriever)  = DynamicListAdapter(contextRetriever)
}