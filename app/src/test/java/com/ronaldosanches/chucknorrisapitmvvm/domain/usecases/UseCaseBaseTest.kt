package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.mock
import com.ronaldosanches.chucknorrisapitmvvm.ObjectResources
import com.ronaldosanches.chucknorrisapitmvvm.domain.repositories.ChuckNorrisJokesRepository
import org.junit.Before

open class UseCaseBaseTest : ObjectResources() {
    lateinit var repository : ChuckNorrisJokesRepository

    @Before
    open fun setup() {
        repository = mock()
    }
}