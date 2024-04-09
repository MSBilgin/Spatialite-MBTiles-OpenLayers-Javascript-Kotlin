package com.msbilgin.smojk.map.data

import android.content.Context
import org.spatialite.database.SQLiteDatabase

class Spatialite private constructor(private val sqLiteDatabase: SQLiteDatabase) {

    private constructor(context: Context) : this(
        SQLiteDatabase.openDatabase(
            GISDataset.spatialite(context).absolutePath,
            null,
            SQLiteDatabase.OPEN_READWRITE
        )
    )

    companion object {

        @Volatile
        private var instance: Spatialite? = null

        fun getIntsance(context: Context): Spatialite? {
            synchronized(this) {
                instance = instance ?: Spatialite(context)
                return instance
            }
        }
    }

    fun getFeatures(
        layerName: String,
        xmin: Double,
        ymin: Double,
        xmax: Double,
        ymax: Double
    ): List<Feature> {
        val featureList = ArrayList<Feature>()

        val sql =
            "select ogc_fid, st_astext(geometry) from $layerName where rowid in " +
                    " (select rowid from spatialindex where f_table_name='$layerName' " +
                    " and search_frame=buildmbr($xmin, $ymin, $xmax, $ymax, 3857))"

        sqLiteDatabase.rawQuery(sql, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    featureList.add(Feature(cursor.getString(0), cursor.getString(1)))
                } while (cursor.moveToNext())
            }
        }

        return featureList
    }
}
