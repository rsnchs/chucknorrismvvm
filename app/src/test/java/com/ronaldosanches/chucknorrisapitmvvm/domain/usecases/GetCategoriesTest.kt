package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.CategoryResponse
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetCategoriesTest : UseCaseBaseTest() {

    lateinit var getCategories : GetCategories

    @Before
    override fun setup() {
        super.setup()
        getCategories = GetCategories(repository)
    }

    @Test
    fun `should get category successful response from repository`() = runBlockingTest {
        //arrange
        whenever(getCategories()).thenReturn(successfulCategoriesList)
        //act
        fun response() = suspend { getCategories() }
        //assert
        assertEquals(response().invoke(),successfulCategoriesList)
        verify(repository).getCategories()
    }

    @Test
    fun `should get error response from repository when error occur`() = runBlockingTest {
        //arrange
        whenever(getCategories()).thenReturn(errorUnknown())
        //act
        fun response() = suspend { getCategories() }
        //assert
        assertEquals(response().invoke(),errorUnknown<CategoryResponse>())
        verify(repository).getCategories()
    }
}