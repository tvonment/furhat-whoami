package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.app.furhatwhoami.services.OpenAIServiceImpl
import furhatos.app.furhatwhoami.shared.GameState
import furhatos.event.Event
import furhatos.flow.kotlin.*
import furhatos.nlu.Intent
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.nlu.intent
import furhatos.snippets.NoInput.examples
import furhatos.util.Language
import kotlin.math.log

val TurnFurhat: State = state(Parent) {
    onEntry {
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

    onResponse<No> {
        GameState.playerOnTurn = GameState.player1
            GameState.thingsyouknow.add(GameState.currentQuestion + " - No.")
            goto(TurnPlayer)
    }

    onResponse<Yes> {
        GameState.thingsyouknow.add(GameState.currentQuestion + " - Yes.")
        reentry()
    }

    onResponse<WonIntent> {
        goto(EndGameWin)
    }

    /*onResponse {
        println("understood: ${it.text}")
        if (it.text.contains("won")) {
            goto(EndGameWin)
        } else if (it.text.lowercase().contains("no")) {

        } else if (it.text.lowercase().contains("yes")) {
            GameState.thingsyouknow.add(GameState.currentQuestion + " - Yes.")
            reentry()
        } else {
            furhat.say("I don't understand. Please answer with yes or no.")
            furhat.listen()
        }
    }*/
}

class WonIntent : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("congrats", "Congratulations, you won.", "Congratulations!", "You won!", "the game is yours", "you won", "you won the game", "you won the game!", "you won the game! Congratulations!", "you won the game! Congrats!", "you won the game! Congrats")
    }
}
class GotFurhatQuestion(val question: String) : Event()