package com.example.cinemaapp.ui

import ActorProfileScreen
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.cinemaapp.data.Actor
import org.junit.Rule
import org.junit.Test

class ActorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun actorNameIsDisplayed() {
        // Arrange
        val testActor = Actor(
            name = "John Doe",
            age = 35,
            bio = "A talented actor",
            img_src = "https://example.com/john_doe.jpg"
        )

        // Act
        composeTestRule.setContent {
            ActorProfileScreen(actor = testActor, onClose = {})
        }

        // Assert
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
    }

    @Test
    fun actorScreen_DisplaysCorrectActorDetails() {
        // Arrange
        val testActor = Actor(
            name = "Robert Downey Jr.",
            age = 58,
            bio = "Known for Iron Man",
            img_src = "https://example.com/iron_man.jpg"
        )

        // Act
        composeTestRule.setContent {
            ActorProfileScreen(actor = testActor, onClose = {})
        }

        // Assert
        composeTestRule.onNodeWithText("Robert Downey Jr.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Age: 58").assertIsDisplayed()
        composeTestRule.onNodeWithText("Known for Iron Man").assertIsDisplayed()
    }

}
