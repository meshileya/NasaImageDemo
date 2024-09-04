package com.nasa.demo.common

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar

/**
 * make view visible
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * make view visible, if onlyInvisible is true, the view takes up the assigned space and not appear
 * other wise, it is gone totally
 */
fun View.hide(onlyInvisible: Boolean = false) {
    visibility = if (onlyInvisible) {
        View.INVISIBLE
    } else {
        View.GONE
    }
}

fun FragmentActivity.toast(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snackbar = Snackbar.make(
        findViewById(android.R.id.content),
        message,
        length
    ).setAction(
        "Ok"
    ) {
    }
    snackbar.show()
}
//fun Context.toast(message: String, length: Int = Toast.LENGTH_LONG) =
//    Toast.makeText(this, message, length).show()

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}