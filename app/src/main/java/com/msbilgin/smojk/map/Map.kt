package com.msbilgin.smojk.map

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.msbilgin.smojk.map.util.Layer

class Map private constructor(private val builder: Builder) {
    private val androidJsBridge = AndroidJSBridge()

    //modules
    val layer = Layer(builder.webView, androidJsBridge)

    private fun start(callback: () -> Unit) {
        builder.webView.settings.javaScriptEnabled = true
        builder.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        builder.webView.addJavascriptInterface(androidJsBridge, AndroidJSBridge.NAME)

        builder.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                androidJsBridge.mapReadyCallback = callback
                builder.webView.loadUrl("javascript:start(${builder.x},${builder.y},${builder.z})")
            }
        }
        //loading page
        builder.webView.loadUrl(AndroidJSBridge.URI)
    }

    class Builder(val webView: WebView) {
        var x: Double = 0.0
            private set
        var y: Double = 0.0
            private set
        var z: Int = 10
            private set

        fun x(x: Double) = apply { this.x = x }
        fun y(y: Double) = apply { this.y = y }
        fun z (z: Int) = apply { this.z = z }

        fun build(callback: (Map) -> Unit) {
            val map = Map(this)
            map.start { callback(map) }
        }
    }
}