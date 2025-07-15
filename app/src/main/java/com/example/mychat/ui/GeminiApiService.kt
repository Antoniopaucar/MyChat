package com.example.mychat.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object GeminiApiService {
    private const val API_KEY = "AIzaSyCoSSD_6rNgNOoDNl2WWuTKrjTVBXrm0jM" // Clave de Gemini proporcionada por el usuario
    private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$API_KEY"
    private val client = OkHttpClient()

    suspend fun getChatResponse(userMessage: String): String = withContext(Dispatchers.IO) {
        val json = JSONObject()
        val contents = org.json.JSONArray()
        val part = JSONObject()
        part.put("text", userMessage)
        val parts = org.json.JSONArray()
        parts.put(part)
        val content = JSONObject()
        content.put("parts", parts)
        contents.put(content)
        json.put("contents", contents)

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(API_URL)
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            if (responseBody != null) {
                val obj = JSONObject(responseBody)
                val candidates = obj.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                    if (contentObj != null) {
                        val partsArr = contentObj.optJSONArray("parts")
                        if (partsArr != null && partsArr.length() > 0) {
                            return@withContext partsArr.getJSONObject(0).optString("text", "(Sin respuesta de IA)")
                        }
                    }
                }
                return@withContext "(Respuesta vacía de IA)"
            } else {
                return@withContext "(Respuesta vacía de IA)"
            }
        } else {
            return@withContext "(Error IA: ${response.code})"
        }
    }
} 