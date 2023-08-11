package com.example.tracklocation.tlUtil

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import com.google.gson.Gson
import org.json.JSONArray
import java.io.IOException


fun String.readFile(context: Context): String {
    val builder = StringBuilder()
    context.assets.open(this.toLowerCase()).bufferedReader().useLines { lines ->
        lines.forEach {
            builder.append(it)
        }
    }
    return builder.toString()
}

fun <T> String.readToObjectList(context: Context, type: Class<T>): MutableList<T> {
    val json = this.readFile(context)
    //val jsonObject = JSONObject(json)
    //val jsonArray = jsonObject.get("sticker_packs") as JSONArray
    val jsonArray = JSONArray(json)
    val list = mutableListOf<T>()
    for (i in 0 until jsonArray.length()) {
        val objStr = jsonArray.getJSONObject(i).toString()
        val t = objStr.fromJson(type)
        list.add(t)
    }


    return list
}
fun <T> String.fromJson(type: Class<T>): T {
    return  Gson().fromJson(this, type)

}
fun String.hasAssetFile(filename: String?, context: Context): Boolean {
    val resources: Resources = context.resources
    val assetManager: AssetManager = resources.getAssets()
    return try {
        assetManager.open(filename!!)
        true
    } catch (iOException: IOException) {
        false
    }
}
