package com.example.waridiresidence.util

import android.view.View
import com.example.waridiresidence.data.model.ValidationResult

object Utils {

    fun validateLoginRequest(email: String, password: String): ValidationResult{
        if (email.isEmpty()) return  ValidationResult(false, "Email is Empty")
        if (password.isEmpty()) return  ValidationResult(false, "Password is Empty")
        return ValidationResult(true)
    }
}