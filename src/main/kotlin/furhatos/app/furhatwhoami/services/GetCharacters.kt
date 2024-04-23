package furhatos.app.furhatwhoami.services

import com.google.gson.Gson
import furhatos.app.furhatwhoami.shared.GameState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

interface GetCharacters {
    fun saveCharacters(callback: (successFull: Boolean) -> Unit)
}

object GetCharactersImpl : GetCharacters {
    override fun saveCharacters(callback: (successFull: Boolean) -> Unit) {
        val shellScript = "/Users/liekenijland/Documents/ITech/ConversationalAgents/furhat-whoami/src/main/kotlin/furhatos/app/furhatwhoami/camera/capture_image.sh"
        executeShellScript(shellScript)

        val client = OkHttpClient();
        val apiUrl = "https://furhat-vision.cognitiveservices.azure.com/computervision/imageanalysis:analyze?api-version=2023-02-01-preview&features=read&language=en&gender-neutral-caption=False"

        try {
            // Read image file as byte array
            val imageFile = File("${System.getenv("USER_PATH")}furhat-whoami/src/main/kotlin/furhatos/app/furhatwhoami/camera/images/furhat_image.jpg") // Replace with your image file path
            val imageData = imageFile.readBytes()

            // Set content type as application/octet-stream
            val octetStreamMediaType = "application/octet-stream".toMediaType()

            val request = Request.Builder()
                .url(apiUrl)
                .header("Content-Type", "application/octet-stream")
                .header("Ocp-Apim-Subscription-Key", System.getenv("VISION_API_KEY"))
                .post(imageData.toRequestBody(octetStreamMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("Unexpected code $response")

                val gson = Gson();
                val responseString = gson.fromJson(response.body?.string(), Response::class.java)
                val words = responseString.readResult.content.lines()

                println("words: ${words}")

                words.forEach { word ->
                    GameState.characters.forEach { character ->
                        if (character.lowercase() == word.lowercase()) {
                            if (GameState.player1.character == "unknown") {
                                GameState.player1.character = word.lowercase();
                            } else if (GameState.player2.character == "unknown") {
                                GameState.player2.character = word.lowercase();
                            }
                        }
                    }
                }
                println(GameState.player1.character)
                println(GameState.player2.character)
                callback(true)
            }
        } catch (e: Exception) {
            println("error: " + e.message)
            callback(false)
        }
    }

    fun executeShellScript(scriptPath: String, vararg params: String) {
        try {
            val command = arrayOf(scriptPath, *params)
            val processBuilder = ProcessBuilder(*command)
            processBuilder.directory(File(scriptPath).parentFile) // Set the working directory
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)

            val process = processBuilder.start() // Start the process
            process.waitFor() // Wait for the process to complete
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    data class Response(
        val readResult: ReadResult,
    )

    data class ReadResult(
        val content: String,
    )
}