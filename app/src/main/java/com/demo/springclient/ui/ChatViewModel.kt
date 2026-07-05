package com.demo.springclient.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.springclient.api.ApiClient
import com.demo.springclient.api.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class ChatViewModel : ViewModel() {

  // Publicly expose UI state while restricting modifications via private set to ensure UDF (MVI principles)
  var messages by mutableStateOf<List<Message>>(emptyList())
    private set

  var loading by mutableStateOf(false)
    private set

  val sessionId = "android_session_${System.currentTimeMillis()}"

  fun sendMessage(context: Context, userMessage: String) {
    if (userMessage.isBlank()) return

    // Synchronously append the user's message to the chat
    messages = messages + Message("user", userMessage)
    loading = true

    // Launch in viewModelScope to handle networking and streaming parsing safely
    viewModelScope.launch {
      try {
        // Fetch the auth token on the IO dispatcher
        val token = withContext(Dispatchers.IO) {
          "Bearer ${TokenManager.getToken(context)}"
        }
        val responseBody = withContext(Dispatchers.IO) {
          ApiClient.service.chatStream(token, userMessage, sessionId)
        }

        // Append an initial empty placeholder message for the AI response
        var aiText = ""
        messages = messages + Message("ai", "")

        // Switch to the IO dispatcher to read and parse the SSE stream
        withContext(Dispatchers.IO) {
          val reader = InputStreamReader(responseBody.byteStream(), "UTF-8")
          val buffer = CharArray(1024)
          var bytesRead: Int
          var sseBuffer = ""

          while (reader.read(buffer).also { bytesRead = it } != -1) {
            sseBuffer += String(buffer, 0, bytesRead)

            while (sseBuffer.contains("\n")) {
              val lineIndex = sseBuffer.indexOf("\n")
              val rawLine = sseBuffer.substring(0, lineIndex)
              sseBuffer = sseBuffer.substring(lineIndex + 1)

              val cleanLine = rawLine.trimStart()
              if (cleanLine.startsWith("data:")) {
                val text = cleanLine.substring(5)
                Log.d("SSE_DEBUG", "chunk: '$text'")

                if (text.isNotEmpty()) {
                  val processedText = text.replace("\\n", "\n")
                  aiText += processedText
                } else {
                  if (aiText.isNotEmpty() && !aiText.endsWith("\n")) {
                    aiText += "\n"
                  }
                }

                val currentAiText = aiText
                // Push to the main thread to refresh the UI
                withContext(Dispatchers.Main) {
                  messages = messages.dropLast(1) + Message("ai", currentAiText)
                }
              }
            }
          }
        }

        Log.d("ChatViewModel", "Stream finished. aiText: $aiText")
      } catch (e: Exception) {
        messages = messages + Message("ai", "Error: ${e.message}")
        Log.e("ChatViewModel", "Failed to send message", e)
      } finally {
        loading = false
      }
    }
  }
}