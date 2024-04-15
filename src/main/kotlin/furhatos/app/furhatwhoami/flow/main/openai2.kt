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
import furhatos.app.furhatwhoami.services.CharactersObject
import furhatos.app.furhatwhoami.services.OpenAIServiceImpl
import furhatos.app.furhatwhoami.shared.GameState
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.math.log

val Openai2: State = state(Parent) {
    onEntry {
        println("Start OpenAI conversation")
        GameState.player1.realName = "Belle"
        GameState.player1.character = "Belle"
        GameState.player2.realName = "Thomas"
        GameState.player2.character = "King Kong"
        println(GameState.player1.realName)
        println(GameState.player1.character)
        println(GameState.player2.realName)
        println(GameState.player2.character)
        OpenAIServiceImpl.sendMessage("player1", "am i an animal?") { response ->
            furhat.say(response)
        }

        val response = runBlocking {
            OpenAIServiceImpl.sendMessage("player2", "am i an animal?", characters) { response ->
                response
            }
        }
        furhat.say(response)
    }
}