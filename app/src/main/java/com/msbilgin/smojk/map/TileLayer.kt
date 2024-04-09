package com.msbilgin.smojk.map

import android.content.Context
import com.msbilgin.smojk.map.data.MBTiles

class TileLayer(private val context: Context) {
    fun getMaxZoom() = MBTiles.getInstance(context)?.getMaxZoom()
    fun getTileBase64(x: Int, y: Int, z: Int) = MBTiles.getInstance(context)?.getBase64(x, y, z)
}