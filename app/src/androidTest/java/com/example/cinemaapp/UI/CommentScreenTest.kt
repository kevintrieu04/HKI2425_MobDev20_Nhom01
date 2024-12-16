package com.example.cinemaapp.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.cinemaapp.data.Comment
import org.junit.Rule
import org.junit.Test
import androidx.compose.runtime.Composable

class CommentScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test: Một bình luận hiển thị đúng
    @Test
    fun commentItem_DisplayedCorrectly() {
        // Arrange
        val testComment = Comment(
            userId = "1",
            userName = "John Doe",
            commentText = "This is a great movie!",
            profileImage = "https://example.com/image.jpg",
            timestamp = System.currentTimeMillis()
        )

        // Act
        composeTestRule.setContent {
            CommentItem(comment = testComment)
        }

        // Assert
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a great movie!").assertIsDisplayed()
    }

    // Test: Danh sách bình luận hiển thị đúng
    @Test
    fun commentList_DisplayedCorrectly() {
        // Arrange
        val testComments = listOf(
            Comment(
                userId = "1",
                userName = "Alice",
                commentText = "Amazing!",
                profileImage = "https://example.com/alice.jpg",
                timestamp = System.currentTimeMillis()
            ),
            Comment(
                userId = "2",
                userName = "Bob",
                commentText = "Not bad!",
                profileImage = "https://example.com/bob.jpg",
                timestamp = System.currentTimeMillis()
            ),
            Comment(
                userId = "3",
                userName = "Charlie",
                commentText = "Could be better...",
                profileImage = "https://example.com/charlie.jpg",
                timestamp = System.currentTimeMillis()
            )
        )

        // Act
        composeTestRule.setContent {
            CommentList(comments = testComments)
        }

        // Assert
        composeTestRule.onNodeWithText("Alice").assertIsDisplayed()
        composeTestRule.onNodeWithText("Amazing!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bob").assertIsDisplayed()
        composeTestRule.onNodeWithText("Not bad!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Charlie").assertIsDisplayed()
        composeTestRule.onNodeWithText("Could be better...").assertIsDisplayed()
    }
}

// Helper Composable cho danh sách bình luận
@Composable
fun CommentList(comments: List<Comment>) {
    androidx.compose.foundation.lazy.LazyColumn {
        items(comments) { comment ->
            CommentItem(comment = comment)
        }
    }
}
