package com.curreny.converter.base.utils

import android.content.Context
import com.curreny.converter.R
import com.curreny.converter.base.utils.detectConnection.ConnectionDetector
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object NetworkComponents {

    var baseUrl: String = "https://api.apilayer.com/fixer/"
    var apiKey: String = "a39xPJ41EWBhJnPCnPf9UPx2ZlyJD0I3"


    fun DETECT_INTERNET_CONNECTION(context: Context): Boolean {
        val cd = ConnectionDetector(context)
        return cd.isConnectingToInternet
    }

    fun showDialog(context: Context) {
        val title = context.getString(R.string.alert)
        val message =
            context.getString(R.string.no_Internet)
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                context.getString(R.string.label_ok)
            ) { dialogInterface, i ->

            }
            .setCancelable(false)
            .show()
    }
}