package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.app.furhatwhoami.services.OpenAIServiceImpl
import furhatos.app.furhatwhoami.shared.GameState
import furhatos.event.Event
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import kotlin.math.log

val TurnFurhat: State = state(Parent) {
    onEntry {
        furhat.say{
            random {
                +"I ask a question."
                +"Ok, I go."
                +"I will ask a question."
                +"Let me think of a question."
            }
        }
        OpenAIServiceImpl.sendMessage("furhat", "your turn") { response ->
            furhat.run {
                raise(GotFurhatQuestion(response))
            }
        }
    }

    onEvent<GotFurhatQuestion>{
        GameState.currentQuestion = it.question
        furhat.ask(it.question)
    }

    /*onResponse<No> {
        GameState.playerOnTurn = GameState.player1
        GameState.thingsyouknow.add(GameState.currentQuestion + " - No.")
        goto(TurnPlayer)
    }

    onResponse<Yes> {
        GameState.thingsyouknow.add(GameState.currentQuestion + " - Yes.")
        reentry()
    }*/

    onResponse {
        println("understood: ${it.text}")
        if (it.text.contains("won")) {
            goto(EndGameWin)
        } else if (it.text.lowercase().contains("no")) {
            GameState.playerOnTurn = GameState.player1
            GameState.thingsyouknow.add(GameState.currentQuestion + " - No.")
            goto(TurnPlayer)
        } else if (it.text.lowercase().contains("yes")) {
            GameState.thingsyouknow.add(GameState.currentQuestion + " - Yes.")
            reentry()
        } else {
            furhat.say("I don't understand. Please answer with yes or no.")
            furhat.listen()
        }
    }
}

class GotFurhatQuestion(val question: String) : Event()