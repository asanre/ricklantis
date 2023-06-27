package io.asanre.app.core.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log


fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    Log.d("findActivity", "Permissions should be called in the context of an Activity")
    return null
}

fun Context.requiredActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}