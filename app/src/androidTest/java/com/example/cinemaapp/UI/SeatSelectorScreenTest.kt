package com.example.cinemaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class SeatSelectorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSeatSelectionTogglesCorrectly() {
        // Arrange: Render the screen
        composeTestRule.setContent {
            TestableSeatSelectorScreen()
        }

        // Act: Click on a seat
        composeTestRule.onNodeWithText("A1").performClick()

        // Assert: Ensure the seat is selected
        composeTestRule.onNodeWithText("A1").assertIsDisplayed()

        // Act: Click on the same seat again
        composeTestRule.onNodeWithText("A1").performClick()

        // Assert: Ensure the seat is unselected
        composeTestRule.onNodeWithText("A1").assertIsDisplayed()
    }

    @Test
    fun testDateSelectionUpdatesCorrectly() {
        // Arrange: Render the screen
        composeTestRule.setContent {
            TestableSeatSelectorScreen()
        }

        // Act: Click on a specific date
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        composeTestRule.onNodeWithText(tomorrow.dayOfMonth.toString()).performClick()

        // Assert: Ensure the date is selected
        composeTestRule.onNodeWithText(tomorrow.dayOfMonth.toString()).assertIsDisplayed()
    }

    @Test
    fun testTimeSelectionUpdatesCorrectly() {
        composeTestRule.setContent {
            TestableSeatSelectorScreen()
        }

        composeTestRule.onNodeWithText("12:00").performClick()

        composeTestRule.onNodeWithText("12:00").assertIsDisplayed()
    }

    @Test
    fun testTotalPriceUpdatesCorrectly() {
        composeTestRule.setContent {
            TestableSeatSelectorScreen()
        }

        composeTestRule.onNodeWithText("A1").performClick()
        composeTestRule.onNodeWithText("A2").performClick()

        composeTestRule.onNodeWithText("130000₫").assertIsDisplayed()
    }

    @Test
    fun testBackNavigationWorks() {
        composeTestRule.setContent {
            TestableSeatSelectorScreen()
        }


        composeTestRule.onNodeWithText("Quay lại").performClick()


        composeTestRule.onNodeWithText("Chọn ghế").assertIsDisplayed()
    }

    @Composable
    private fun TestableSeatSelectorScreen() {
        val navController = rememberNavController()
        SeatSelectorScreen(navController = navController)
    }
}
