package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.app.furhatwhoami.services.OpenAIServiceImpl
import furhatos.app.furhatwhoami.shared.GameState
import furhatos.event.Event
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val TurnPlayer: State = state(Parent) {
    onEntry {
        val player = GameState.playerOnTurn
        furhat.say("its your turn ${player.realName}")
        furhat.listen()

    }
    onResponse{
        println("understood: ${it.text}")
        val player = GameState.playerOnTurn
        println("character: ${player.character}")
        if (it.text.contains(player.character.lowercase())) {
            goto(EndGameLose)
        }
        OpenAIServiceImpl.sendMessage(player.character, it.text) { response ->
            furhat.run {
                raise(GotFurhatAnswer(response))
            }
        }
    }

    onEvent<GotFurhatAnswer>{
        furhat.say(it.answer)
        if (it.answer.lowercase().contains("no")) {
            if (GameState.playerOnTurn == GameState.player1) {
                GameState.playerOnTurn = GameState.player2
                furhat.say("It's your turn ${GameState.player2.realName}")
                reentry()
            } else {
                GameState.playerOnTurn = GameState.player1
                furhat.say("Ok. Then it's my turn.")
                goto(TurnFurhat)
            }
        } else {
            reentry()
        }
    }
}

class GotFurhatAnswer(val answer: String) : Event()