package com.msbilgin.smojk.map

import android.webkit.JavascriptInterface


class AndroidJSBridge {
    companion object {
        const val NAME = "AndroidJS"
        const val URI = "file:///android_asset/map/index.html";
    }

    var mapReadyCallback: (() -> Unit)? = null
    var getTileCallback: ((x: Int, y: Int, z: Int) -> Unit)? = null
    var getFeaturesCallback: ((layerName: String, xmin: Double, ymin: Double, xmax: Double, ymax: Double) -> Unit)? =
        null

    @JavascriptInterface
    fun mapReady() {
        mapReadyCallback?.invoke()
    }

    @JavascriptInterface
    fun getTile(x: Int, y: Int, z: Int) {
        getTileCallback?.invoke(x, y, z)
    }

    @JavascriptInterface
    fun getFeatures(layerName: String, xmin: Double, ymin: Double, xmax: Double, ymax: Double) {
        getFeaturesCallback?.invoke(layerName, xmin, ymin, xmax, ymax)
    }

}