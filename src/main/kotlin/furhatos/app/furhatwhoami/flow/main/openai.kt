package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.event.Event
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.gestures.Gestures
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

val Openai: State = state(Parent) {
    var history: List<ChatHistoryItem> = emptyList()
    val promptFlowUrl = System.getenv("PROMPT_API_URL")
    val promptFlowKey = System.getenv("PROMPT_API_KEY")
    val promptFlowModel = System.getenv("PROMPT_API_MODEL")

    onEntry {
        furhat.ask("Hello")
    }

    onResponse {
        furhat.gesture(Gestures.Thoughtful)
        val client = OkHttpClient()
        // Create a Moshi instance
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        // Create a JSON adapter for MessageRequest class
        val jsonAdapter = moshi.adapter(MessageRequest::class.java)

        // Create a MessageRequest object with provided JSON structure
        val request = MessageRequest(
                history,
                it.text
        )

        // Convert the MessageRequest object to a JSON string
        val jsonBody = jsonAdapter.toJson(request)
        val httpRequest = Request.Builder()
                .url(promptFlowUrl)
                .addHeader("Authorization", "Bearer $promptFlowKey")
                .addHeader("azureml-model-deployment", promptFlowModel)
                .addHeader("Content-Type", "application/json")
                .post(jsonBody.toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

        client.newCall(httpRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the failure of the request
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle the response of the request
                val responseData = response.body?.string()

                if (responseData != null) {
                    // Create a JSON adapter for JsonResponse class
                    val jsonResponseAdapter = moshi.adapter(JsonResponse::class.java)

                    try {
                        // Parse the JSON string to JsonResponse object
                        val jsonResponse = jsonResponseAdapter.fromJson(responseData)

                        if (jsonResponse != null) {
                            // Process the JsonResponse here, e.g., print the message content
                            history = history + (ChatHistoryItem(
                                    InputItem(it.text),
                                    OutputItem(jsonResponse.reply),
                            ))

                            furhat.run {
                                raise(GotAnswer(jsonResponse.reply))
                            }
                        }
                    } catch (e: Exception) {
                        // Handle the error during JSON parsing
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    // Listen for the GetAnswer event and specify the behavior that should follow
    onEvent<GotAnswer> {
        // Start a new interaction
        furhat.say(it.reply)
        delay(2000)
        reentry() // Go back to the previous state to ask for another joke
    }
}
class GotAnswer(val reply: String) : Event()

data class MessageRequest(
        @Json(name = "chat_history")
        val chatHistory: List<ChatHistoryItem>,
        val input: String
)

data class ChatHistoryItem(
        val inputs: InputItem,
        val outputs: OutputItem,
)

data class OutputItem(
        val reply: String
)

data class InputItem(
        val input: String
)

data class JsonResponse(
        val reply: String,
)