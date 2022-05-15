package com.kakaogames.gd.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun kakIsInternetAvailable(context: Context): Boolean {
    val kakConnectivityManager =
        ContextCompat.getSystemService(context, ConnectivityManager::class.java)
    val kakActiveNetwork = kakConnectivityManager?.activeNetwork ?: return false
    val kakNetworkCapabilities =
        kakConnectivityManager.getNetworkCapabilities(kakActiveNetwork) ?: return false
    return when {
        kakNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        kakNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        kakNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun kakShowErrorDialog(context: Context, action: () -> Unit, message: String?): AlertDialog =
    MaterialAlertDialogBuilder(context).setTitle("Error")
        .setMessage("$message")
        .setPositiveButton("Try again") { dialog, _ ->
            action()
            dialog.dismiss()
        }
        .show()

fun String.vigenere(encrypt: Boolean = false): String {
    val sb = StringBuilder()
    val lsbdpaxvht = "comkakaogamesgd"
    var index = 0

    this.forEach {
        if (it !in 'a'..'z'){
            sb.append(it)
            return@forEach
        }
        val wlfgflga = if (encrypt) {
            (it.code + lsbdpaxvht[index].code - 90) % 26
        } else {
            (it.code - lsbdpaxvht[index].code + 26) % 26
        }
        index++
        if (index > lsbdpaxvht.length - 1) index = 0
        sb.append(wlfgflga.plus(97).toChar())
    }
    return sb.toString()
}
