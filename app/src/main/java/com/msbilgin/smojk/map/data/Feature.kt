package com.msbilgin.smojk.map.data

class Feature(val id: String, val wkt: String) {

    fun jsFriendly(): String = "$id;$wkt"
}