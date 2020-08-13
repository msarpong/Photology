package org.msarpong.photology.util

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun handleUrl(url: String): String {
    val uri = Uri.parse(url)
    return if (url.contains("code")) {
        val requestCode = uri.getQueryParameter("code") ?: ""
        Log.d("handleUrl", requestCode)
        requestCode
    } else {
        "false"
    }
}

fun convertDate(oldDate: String): LocalDate? {
    val formatter = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH)
    return LocalDate.parse(oldDate, formatter)
}

val LOCALE: Locale = Locale.ITALIAN

fun formatDate(oldDate: String, originalFormat: String, outputFormat: String): String {
    val date = LocalDateTime.parse(oldDate, DateTimeFormatter.ofPattern(originalFormat))
    return date.format(DateTimeFormatter.ofPattern(outputFormat, LOCALE))
}

fun forDate(dateString: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("dd")
    val formattedDate = formatter.format(parser.parse(dateString))
    return formattedDate
}