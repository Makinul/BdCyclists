package com.bd.cyclists

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    // Example for a simple function in your app code
    fun addNumbers(a: Int, b: Int): Int = a + b

    @Test
    fun addNumbers_returnsCorrectSum() {
        val result = addNumbers(5, 3)
        assertEquals(8, result)
    }
}