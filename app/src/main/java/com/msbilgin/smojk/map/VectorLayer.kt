package com.msbilgin.smojk.map

import android.content.Context
import com.msbilgin.smojk.map.data.Feature
import com.msbilgin.smojk.map.data.Spatialite

class VectorLayer(private val context: Context, val layerName: String, val styleName: String) {
    fun getFeatures(
        xmin: Double,
        ymin: Double,
        xmax: Double,
        ymax: Double
    ): List<Feature> =
        Spatialite.getIntsance(context)?.getFeatures(layerName, xmin, ymin, xmax, ymax)!!
}