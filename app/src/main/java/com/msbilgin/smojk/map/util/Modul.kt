package com.msbilgin.smojk.map.util

import android.app.Activity
import android.webkit.WebView

abstract class Modul(protected val webView: WebView) {

    private val activity = webView.context as Activity

    protected fun runJS(fn: String, vararg params: String) {
        var callableFN = fn

        for (param in params) {
            callableFN = callableFN.replaceFirst("\\?".toRegex(), param)
        }

        activity.runOnUiThread { webView.loadUrl("javascript:$callableFN") }
    }
}