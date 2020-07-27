package org.msarpong.splash.util

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

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