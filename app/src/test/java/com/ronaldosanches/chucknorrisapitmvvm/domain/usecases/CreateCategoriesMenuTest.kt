package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class CreateCategoriesMenuTest : UseCaseBaseTest() {

    private lateinit var createCategoriesMenu: CreateCategoriesMenu

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun `when adding array of strings should add title and wrap with genetic list item`() = runTest {
        //arrange
        val categoryString = "category 1"
        val categories = arrayOf(categoryString)

        //act
        val actual = createCategoriesMenu(categories)

        assertTrue(actual.size == 2)
        assertTrue(actual.first() is SectionTitleItem)
        assertTrue(actual[1] is GenericListItem)
    }
}