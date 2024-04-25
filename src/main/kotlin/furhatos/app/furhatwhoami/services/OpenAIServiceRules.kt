package furhatos.app.furhatwhoami.services

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import furhatos.app.furhatwhoami.shared.GameState
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
/*
interface OpenAIRulesService {
    fun sendMessage(message: String, callback: (response: String) -> Unit)
}
object OpenAIRulesServiceImpl : OpenAIService {
    /*val promptFlowUrl = System.getenv("PROMPT_API_URL")
    val promptFlowKey = System.getenv("PROMPT_API_KEY")
    val promptFlowModel = System.getenv("PROMPT_API_MODEL")*/
    val promptFlowUrl = System.getenv("PROMPT_API_URL")
    val promptFlowKey = System.getenv("PROMPT_API_KEY")
    val promptFlowModel = System.getenv("PROMPT_API_MODEL")
    val client = OkHttpClient()
    // Create a Moshi instance
    val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    // Implement the sendMessage method
    override fun sendMessage(message: String, callback: (response: String) -> Unit) {
        // Create a JSON adapter for MessageRequest class
        val jsonAdapter = moshi.adapter(QuestionRequest::class.java)
        val characters = GameState.characters
        val request = QuestionRequest(
                emptyList(),
                message,
                characters
        )
        // Create the HTTP request
        // Convert the MessageRequest object to a JSON string
        val jsonBody = jsonAdapter.toJson(request)
        println("Request: $jsonBody")
        val httpRequest = Request.Builder()
                .url(promptFlowUrl)
                .addHeader("Authorization", "Bearer $promptFlowKey")
                .addHeader("azureml-model-deployment", promptFlowModel)
                .addHeader("Content-Type", "application/json")
                .post(jsonBody.toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

        // Execute the HTTP request asynchronously
        client.newCall(httpRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                println("Failed to execute request: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle success
                val responseData = response.body?.string()
                println("Response: $responseData")
                val jsonResponseAdapter = moshi.adapter(AnswerResponse::class.java)
                val jsonResponse = responseData?.let { jsonResponseAdapter.fromJson(it) }
                jsonResponse?.let {
                    // Add to history and execute the callback with the answer
                    callback(it.answer)
                }
            }
        })
    }
}
data class QuestionRequest(
        @Json(name = "chat_history")
        val chatHistory: List<HistoryItem>,
        val question: String,
        val characters: Array<String>,
)

data class HistoryItem(
        val inputs: InputItem,
        val outputs: OutputItem,
)

data class AnswerResponse(
        var answer: String
)*/