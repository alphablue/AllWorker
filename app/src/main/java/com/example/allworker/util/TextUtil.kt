package com.example.allworker.util

object TextUtil {
    fun isValidEmail(email: String): Boolean {
        val regexOption = """^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$""".toRegex()

        return regexOption matches email
    }

    fun isValidPassword(password: String): Boolean {
        val regexOption = """^([\d+a-z]+){6,13}$""".toRegex()

        return regexOption matches password
    }
}