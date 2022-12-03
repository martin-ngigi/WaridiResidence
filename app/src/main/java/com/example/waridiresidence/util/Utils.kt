package com.example.waridiresidence.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
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

    fun validateProfileRequest(firstname: String,
                                lastname: String,
                                phone: String,
                               //imageUri: String,
    ): ValidationResult{
        if (firstname.isEmpty()) return  ValidationResult(false, "First Name is Empty")
        if (lastname.isEmpty()) return  ValidationResult(false, "Last Name is Empty")
        if (phone.isEmpty()) return  ValidationResult(false, "Phone is Empty")
            // if (imageUri.isEmpty()) return  ValidationResult(false, "Image is Empty")

        //else
        return ValidationResult(true)

    }

    fun Context.toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun toast2(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        //eg
        //toast2(requireContext(), "HI")
    }
}