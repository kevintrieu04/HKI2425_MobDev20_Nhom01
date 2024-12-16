package com.example.cinemaapp.data

import org.junit.Assert.*
import org.junit.Test

class ActorTest {

    @Test
    fun `check default values of Actor`() {
        val actor = Actor()
        assertEquals("", actor.id)
        assertEquals("", actor.name)
        assertEquals(0, actor.age)
        assertEquals("", actor.bio)
        assertEquals("", actor.img_src)
    }

    @Test
    fun `create actor with valid inputs`() {
        val actor = Actor(
            id = "123",
            name = "John Doe",
            age = 30,
            bio = "A great actor",
            img_src = "http://example.com/image.jpg"
        )

        assertEquals("123", actor.id)
        assertEquals("John Doe", actor.name)
        assertEquals(30, actor.age)
        assertEquals("A great actor", actor.bio)
        assertEquals("http://example.com/image.jpg", actor.img_src)
    }
}
