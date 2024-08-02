package io.trading.bot.storage.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class PreferenceStorage(private val dataStore: DataStore<Preferences>) {

    suspend fun setValue(key: String, value: String) {
        dataStore.updateData {
            it.toMutablePreferences().apply { set(stringPreferencesKey(key), value) }
        }
    }

    suspend fun getValue(key: String): String? {
        return dataStore.data.firstOrNull()?.get(stringPreferencesKey(key))
    }
}

suspend inline fun PreferenceStorage.setData(key: String, value: Any) {
    setValue(key, Json.encodeToString(value))
}

suspend inline fun <reified T> PreferenceStorage.getData(key: String): T? {
    return getValue(key)?.let { Json.decodeFromString(it) }
}
