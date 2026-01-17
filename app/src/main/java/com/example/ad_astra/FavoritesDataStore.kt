package com.example.ad_astra

import FavoriteApod
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

private val Context.dataStore by preferencesDataStore("favorites")

class FavoritesRepository(private val context: Context) {

    private val FAVORITES_KEY = stringPreferencesKey("favorites_json")

    val favorites: Flow<List<FavoriteApod>> =
        context.dataStore.data.map { prefs ->
            val json = prefs[FAVORITES_KEY] ?: "[]"
            val array = JSONArray(json)

            List(array.length()) { i ->
                val obj = array.getJSONObject(i)
                FavoriteApod(
                    date = obj.getString("date"),
                    title = obj.getString("title"),
                    imageUrl = obj.optString("imageUrl").ifBlank { null },
                    mediaType = obj.getString("mediaType")
                )
            }
        }

    suspend fun toggleFavorite(apod: ApodResponse) {
        context.dataStore.edit { prefs ->
            val current = JSONArray(prefs[FAVORITES_KEY] ?: "[]")

            val index = (0 until current.length()).firstOrNull {
                current.getJSONObject(it).getString("date") == apod.date
            }

            if (index != null) {
                current.remove(index)
            } else {
                val obj = JSONObject().apply {
                    put("date", apod.date)
                    put("title", apod.title)
                    put("imageUrl", getApodImageUrl(apod))
                    put("mediaType", apod.mediaType)
                }
                current.put(obj)
            }

            prefs[FAVORITES_KEY] = current.toString()
        }
    }
}
