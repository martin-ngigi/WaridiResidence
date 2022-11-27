package com.example.waridiresidence.util

import android.view.View
import com.example.waridiresidence.data.model.ValidationResult

object Utils {

    fun validateLoginRequest(email: String, password: String): ValidationResult{
        if (email.isEmpty()) return  ValidationResult(false, "Email is Empty")
        if (password.isEmpty()) return  ValidationResult(false, "Password is Empty")
        return ValidationResult(true)
    }

    fun validateRegisterRequest(firstname: String,
                                lastname: String,
                                email: String,
                                phone: String,
                                password: String
    ): ValidationResult{
        if (firstname.isEmpty()) return  ValidationResult(false, "First Name is Empty")
        if (lastname.isEmpty()) return  ValidationResult(false, "Last Name is Empty")
        if (email.isEmpty()) return  ValidationResult(false, "Email is Empty")
        if (phone.isEmpty()) return  ValidationResult(false, "Phone is Empty")
        if (password.isEmpty()) return  ValidationResult(false, "Password is Empty")

        //else
        return ValidationResult(true)

    }
}