package com.msbilgin.smojk.map.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Base64

class MBTiles private constructor(private val sqliteDatabase: SQLiteDatabase) {

    private constructor(context: Context) : this(
        SQLiteDatabase.openDatabase(
            GISDataset.mbtiles(context).absolutePath,
            null,
            SQLiteDatabase.OPEN_READONLY
        )
    )

    companion object {

        @Volatile
        private var instance: MBTiles? = null

        fun getInstance(context: Context): MBTiles? {
            synchronized(this) {
                instance = instance ?: MBTiles(context)
                return instance
            }
        }
    }

    fun getBase64DataURL(x: Int, y: Int, z: Int): String? {
        val yy = (Math.pow(2.0, z.toDouble()) - 1 - y).toInt()
        val sql =
            "select tile_data from tiles where zoom_level=$z and tile_column=$x and tile_row=$yy"

        sqliteDatabase.rawQuery(sql, null).use { cursor ->
            if (cursor?.moveToFirst() == true) {
                val data = cursor.getBlob(0);
                val base64 = Base64.encodeToString(data, Base64.NO_WRAP)
                val mime = if (base64.startsWith("/9g")) "image/jpg" else "image/png"
                val base64DataURL = "data:$mime;base64,$base64";
                return base64DataURL;
            } else {
                return null;
            }
        }
    }

    fun getMaxZoom(): Int {
        val sql = "select max(zoom_level) from tiles"
        sqliteDatabase.rawQuery(sql, null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
    }
}