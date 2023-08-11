package com.example.phonenumberlocator.pnlUtil


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.phonenumberlocator.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PNLDataStoreDb (val context: Context) {

    private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
        name = context.getString(
            R.string.app_name
        ) + "Db"
    )


    private val runOnDefault
        get() = CoroutineScope(Dispatchers.IO)
    private val runOnMain
        get() = CoroutineScope(Dispatchers.Main)

    fun saveString(fileName: String, value: String?) {
        val dataStoreKey = stringPreferencesKey(name = fileName)
        runOnDefault.launch {
            context.dataStore.edit { dataBase ->
                value?.let { dataBase[dataStoreKey] = it }
            }
        }
    }

    fun getString(fileName: String, value: (String?) -> Unit) {
        val dataStoreKey = stringPreferencesKey(name = fileName)
        runOnMain.launch {
            context.dataStore.edit { dataBase ->
                value.invoke(dataBase[dataStoreKey] ?: "Empty")
            }
        }
    }

    fun saveInt(fileName: String, value: Int?) {
        val dataStoreKey = intPreferencesKey(name = fileName)
        runOnDefault.launch {
            context.dataStore.edit { dataBase ->
                value?.let { dataBase[dataStoreKey] = it }
            }
        }
    }

    fun getInt(fileName: String, value: (Int?) -> Unit) {
        val dataStoreKey = intPreferencesKey(name = fileName)
        runOnMain.launch {
            context.dataStore.edit { dataBase ->
                value.invoke(dataBase[dataStoreKey] ?: 0)
            }
        }
    }

    fun saveDouble(fileName: String, value: Double?) {
        val dataStoreKey = doublePreferencesKey(name = fileName)
        runOnDefault.launch {
            context.dataStore.edit { dataBase ->
                value?.let { dataBase[dataStoreKey] = it }
            }
        }
    }

    fun getDouble(fileName: String, value: (Double?) -> Unit) {
        val dataStoreKey = doublePreferencesKey(name = fileName)
        runOnMain.launch {
            context.dataStore.edit { dataBase ->
                value.invoke(dataBase[dataStoreKey] ?: 0.0)
            }
        }
    }

    fun saveFloat(fileName: String, value: Float?) {
        val dataStoreKey = floatPreferencesKey(name = fileName)
        runOnDefault.launch {
            context.dataStore.edit { dataBase ->
                value?.let { dataBase[dataStoreKey] = it }
            }
        }
    }

    fun getFloat(fileName: String, value: (Float?) -> Unit) {
        val dataStoreKey = floatPreferencesKey(name = fileName)
        runOnMain.launch {
            context.dataStore.edit { dataBase ->
                value.invoke(dataBase[dataStoreKey] ?: 0.0F)
            }
        }
    }

    fun saveLong(fileName: String, value: Long?) {
        val dataStoreKey = longPreferencesKey(name = fileName)
        runOnDefault.launch {
            context.dataStore.edit { dataBase ->
                value?.let { dataBase[dataStoreKey] = it }
            }
        }
    }

    fun getLong(fileName: String, value: (Long?) -> Unit) {
        val dataStoreKey = longPreferencesKey(name = fileName)
        runOnMain.launch {
            context.dataStore.edit { dataBase ->
                value.invoke(dataBase[dataStoreKey] ?: 0)
            }
        }
    }

    fun saveBoolean(fileName: String, value: Boolean?) {
        val dataStoreKey = booleanPreferencesKey(name = fileName)
        runOnDefault.launch {
            context.dataStore.edit { dataBase ->
                value?.let { dataBase[dataStoreKey] = it }
            }
        }
    }

    fun getBoolean(fileName: String, value: (Boolean?) ->Unit) {
        val dataStoreKey = booleanPreferencesKey(name = fileName)
        runOnMain.launch {
            context.dataStore.edit { dataBase ->
                value.invoke(dataBase[dataStoreKey] ?: false)
            }
        }
    }
}