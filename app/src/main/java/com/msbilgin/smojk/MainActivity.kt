package com.msbilgin.smojk

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.msbilgin.smojk.map.Map
import com.msbilgin.smojk.map.TileLayer
import com.msbilgin.smojk.map.VectorLayer

class MainActivity : AppCompatActivity() {

    private lateinit var map: Map
    private lateinit var layerRailway: VectorLayer
    private lateinit var layerTaxi: VectorLayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMap()
    }

    private fun initMap() {
        val tileLayer = TileLayer(this)
        layerRailway = VectorLayer(this, "railway", "RAILWAY")
        layerTaxi = VectorLayer(this, "taxi_stands", "TAXI_STANDS")

        Map.Builder(findViewById(R.id.webview))
            .x(3235237.0)
            .y(5035567.0)
            .z(12)
            .build { map ->
                this.map = map
                map.layer.setTileLayer(tileLayer)
                map.layer.addVectorLayer(layerRailway)
                map.layer.addVectorLayer(layerTaxi)
            }
    }
}