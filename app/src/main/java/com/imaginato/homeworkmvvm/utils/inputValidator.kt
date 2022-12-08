package com.imaginato.homeworkmvvm.utils

import com.imaginato.homeworkmvvm.ui.base.IApp

/**
 * Created by Parth Soni on 12/8/2022.
 */
object inputValidator {

    fun validateLogin(userName: String, password: String): Boolean {
        if (userName.isEmpty()) {
            return false
        }
        if (password.isEmpty()) {
            return false
        }
        return true
    }
}