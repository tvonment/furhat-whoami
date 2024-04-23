package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.app.furhatwhoami.services.OpenAIServiceImpl
import furhatos.app.furhatwhoami.shared.GameState
//import furhatos.app.furhatwhoami.flow.Parent
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
//import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val EndGameLose : State = state(Parent){
    onEntry {
        //if player 1 or 2 wins
        furhat.gesture(Gestures.ExpressAnger)
        furhat.say{
            random {
                +"Congratulations winner ${GameState.playerOnTurn.realName},you won!"
                +"Well, well, that was quick. ${GameState.playerOnTurn.realName}, you won"
                +"Ok. You deserve it, congrats ${GameState.playerOnTurn.realName}"
                +"Ahh, I almost had it. But congrats to ${GameState.playerOnTurn.realName}!"
                +"Ah, come on. This is unfair, ${GameState.playerOnTurn.realName}'s character was way easier then mine."
            }
        }
        delay(1000)
        furhat.ask("Do you want to play again?")
    }
    onResponse<Yes> {
        furhat.say("Great! Let's play again.")
        goto(BeginGame)
    }
    onResponse<No> {
        furhat.say("Ok. Thank you for playing with me. ByeBye")
    }

}

val EndGameWin : State = state(Parent){
    onEntry {
        //if furhat wins
        furhat.gesture(Gestures.BigSmile)
        furhat.say{
            random {
                +"I guess we have a winner then, hahahaaa"
                +"I won! Yes!"
                +"Easy peasy, I won this game."
                +"Thank you for being such good competitors."
            }
        }
        furhat.ask("Do you want to loose again?")
    }
    onResponse<Yes> {
        furhat.say("Great! Let's play again.")
        goto(BeginGame)
    }
    onResponse<No> {
        furhat.say("Ok. Thank you for playing with me. ByeBye")
    }
}