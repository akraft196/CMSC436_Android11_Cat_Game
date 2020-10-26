package com.example.lab7_firebase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TestValidation {

    private val validator = Validators()
    // TODO: Test passwords
    // Add two tests for a valid password
    @Test
    fun testValidPass1() {
        val password = "pAssw0rd"
        assertTrue(validator.validPassword(password))
    }

    @Test
    fun testValidPass2() {
        val password = "a1234567"
        assertTrue(validator.validPassword(password))
    }

    // Add two tests for an invalid password
    @Test
    fun testInvalidPass1(){
        val password = "sh0"
        assertFalse(validator.validPassword(password))
    }

    @Test
    fun testInvalidPass2(){
        val password = "alletter"
        assertFalse(validator.validPassword(password))
    }

    @Test
    fun testInvalidPass3(){
        val password = "1234567"
        assertFalse(validator.validPassword(password))
    }

    @Test
    fun testInvalidPass4(){
        val password = "TooL0ngPassword"
        assertFalse(validator.validPassword(password))
    }
}