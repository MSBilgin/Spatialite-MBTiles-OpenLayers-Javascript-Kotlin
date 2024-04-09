package com.msbilgin.smojk.map.data

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class GISDataset {

    companion object {
        private const val FOLDERNAME = "gis_data"
        private const val MBTILES_FILENAME = "basemap.mbtiles"
        private const val SPATIALITE_FILENAME = "istanbul.sqlite"

        private fun init(context: Context) {
            val internalFolder = File(context.filesDir, FOLDERNAME)

            if (!internalFolder.exists()) {
                internalFolder.mkdir()
                context.assets.list(FOLDERNAME)?.toList()?.forEach { fileName ->
                    context.assets.open("$FOLDERNAME/$fileName").use { src ->
                        val destFile = File(internalFolder, fileName)
                        FileOutputStream(destFile).use { dest ->
                            src.copyTo(dest)
                        }
                    }
                }
            }
        }

        fun mbtiles(context: Context): File {
            init(context)
            val internalFolder = File(context.filesDir, FOLDERNAME)
            return internalFolder.let { File(it, MBTILES_FILENAME) }
        }

        fun spatialite(context: Context): File {
            init(context)
            val internalFolder = File(context.filesDir, FOLDERNAME)
            return internalFolder.let { File(it, SPATIALITE_FILENAME) }
        }
    }
}