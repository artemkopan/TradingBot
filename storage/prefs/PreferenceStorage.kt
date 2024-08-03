package io.trading.bot.storage.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class PreferenceStorage(private val dataStore: DataStore<Preferences>) {

    suspend fun setValue(key: String, value: String) {
        dataStore.updateData {
            it.toMutablePreferences().apply { set(stringPreferencesKey(key), value) }
        }
    }

    suspend fun getValue(key: String): String? {
        val valueOrNull = dataStore.data.firstOrNull()
        return valueOrNull?.get(stringPreferencesKey(key))
    }
}

suspend inline fun <reified T> PreferenceStorage.setData(key: String, value: T) {
    val encodeToString = Json.encodeToString(value)
    Timber.d("setData() called with: key = $key, value = $value, encodeToString = $encodeToString")
    setValue(key, encodeToString)
}

suspend inline fun <reified T> PreferenceStorage.getData(key: String): T? {
    val value = getValue(key)
    Timber.d("Data value: $value")
    return value?.let { Json.decodeFromString(it) }
}
