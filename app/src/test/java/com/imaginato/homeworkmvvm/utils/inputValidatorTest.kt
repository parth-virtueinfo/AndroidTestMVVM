package com.imaginato.homeworkmvvm.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Parth Soni on 12/8/2022.
 */
@RunWith(JUnit4::class)
class inputValidatorTest {

    @Test
    fun whenInputIsValid() {
        val userName = "adasff"
        val password = "123124"
        val result = inputValidator.validateLogin(userName, password)
        assertThat(result).isTrue()
    }

    @Test
    fun whenUserNameEmpty() {
        val userName = ""
        val password = "123124"
        val result = inputValidator.validateLogin(userName, password)
        assertThat(result).isFalse()
    }

    @Test
    fun whenPasswordEmpty() {
        val userName = "erewwt"
        val password = ""
        val result = inputValidator.validateLogin(userName, password)
        assertThat(result).isFalse()
    }

    @Test
    fun whenBothEmpty() {
        val userName = ""
        val password = ""
        val result = inputValidator.validateLogin(userName, password)
        assertThat(result).isFalse()
    }

}