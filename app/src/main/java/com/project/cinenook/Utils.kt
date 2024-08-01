package com.project.cinenook

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utils {
    fun formatReleaseDate(dateString: String?): String {
        val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterOutput = DateTimeFormatter.ofPattern("EE, dd MMM yyyy", Locale("en"))

        return dateString?.let {
            try {
                val date = LocalDate.parse(it, formatterInput)
                date.format(formatterOutput)
            } catch (e: Exception) {
                "Invalid date"
            }
        } ?: "Date not available"
    }

}