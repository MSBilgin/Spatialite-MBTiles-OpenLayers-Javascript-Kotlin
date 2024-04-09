package com.msbilgin.smojk.map.util

import android.text.TextUtils
import android.webkit.WebView
import com.msbilgin.smojk.map.TileLayer
import com.msbilgin.smojk.map.AndroidJSBridge
import com.msbilgin.smojk.map.VectorLayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class Layer(webView: WebView, androidJsBridge: AndroidJSBridge) : Modul(webView) {
    private val vectorLayers = ArrayList<VectorLayer>()
    private lateinit var tileLayer: TileLayer

    init {
        androidJsBridge.getTileCallback = { x, y, z -> GlobalScope.async { loadTile(x, y, z) } }
        androidJsBridge.getFeaturesCallback =
            { layerName, xmin, ymin, xmax, ymax ->
                GlobalScope.async {
                    loadFeatures(
                        layerName,
                        xmin,
                        ymin,
                        xmax,
                        ymax
                    )
                }
            }
    }

    fun setTileLayer(tileLayer: TileLayer) {
        this.tileLayer = tileLayer
        runJS("Utils.layer.addTileLayer(${tileLayer.getMaxZoom()})")
    }

    fun addVectorLayer(vectorLayer: VectorLayer) {
        vectorLayers.add(vectorLayer)
        runJS("Utils.layer.addVectorLayer('?','?')", vectorLayer.layerName, vectorLayer.styleName)
    }

    fun setEnabled(vectorLayer: VectorLayer, status: Boolean) {
        runJS("Utils.layer.setEnabled('?',?)", vectorLayer.layerName, status.toString())
    }

    private fun loadFeatures(
        layerName: String,
        xmin: Double,
        ymin: Double,
        xmax: Double,
        ymax: Double
    ) {
        val containsNaN = java.lang.Double.isNaN(xmin) || java.lang.Double.isNaN(ymin)
                || java.lang.Double.isNaN(xmax) || java.lang.Double.isNaN(ymax)

        if (!containsNaN) {
            val data = ArrayList<String>()
            var counter = 0
            val sizeLimit = 30000

            vectorLayers.filter { v -> v.layerName == layerName }[0]
                .getFeatures(xmin, ymin, xmax, ymax).let { features ->
                    for (feature in features) {
                        if (counter < sizeLimit) {
                            val i = feature.jsFriendly()
                            data.add(i)
                            counter += i.length
                        } else {
                            val jsArray = "['${TextUtils.join("','", data)}']"
                            runJS("Utils.layer.loadFeatureData('?', ?)", layerName, jsArray)
                            data.clear()
                            counter = 0
                        }
                    }

                    if (!data.isEmpty()) {
                        val jsArray = "['${TextUtils.join("','", data)}']"
                        runJS("Utils.layer.loadFeatureData('?', ?)", layerName, jsArray)
                    }
                }
        }
    }

    private fun loadTile(x: Int, y: Int, z: Int) {
        val base64 = tileLayer.getTileBase64(x, y, z)
        if (base64 == null) {
            runJS(
                "Utils.layer.loadTileData(?,?,?,null)",
                x.toString(),
                y.toString(),
                z.toString()
            )
        } else {
            runJS(
                "Utils.layer.loadTileData(?,?,?,'?')",
                x.toString(),
                y.toString(),
                z.toString(),
                base64
            )
        }
    }


}