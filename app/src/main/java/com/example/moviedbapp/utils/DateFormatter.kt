package com.example.moviedbapp.utils

import java.text.NumberFormat
import java.util.Locale


object DateFormatter {

    operator fun invoke(n: Long): String {
        val locale = Locale("en", "EN")
        val numberFormat = NumberFormat.getNumberInstance(locale)
        return numberFormat.format(n)
    }

}