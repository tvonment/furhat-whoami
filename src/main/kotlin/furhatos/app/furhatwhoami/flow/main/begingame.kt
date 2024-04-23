package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.app.furhatwhoami.services.OpenAIServiceImpl
import furhatos.app.furhatwhoami.shared.GameState
import furhatos.app.furhatwhoami.services.GetCharactersImpl
import furhatos.event.Event
//import furhatos.app.furhatwhoami.flow.Parent
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
//import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
//import okhttp3.internal.wait
//import java.awt.SystemColor.text

//give assignment to write cards
val BeginGame : State = state(Parent){
    onEntry {
//        TODO: explain about the list of characters where they should choose from
        furhat.say {
            random {
                +"Let's first write the cards, one for each of us."
                +"Let's first create cards with characters, one for each player including me."
            }
            random {
                +"keep the cards a secret!"
                +"keep the cards to yourself!"
                +"don't show the cards to yet."
            }
        }
        delay(5000)
        furhat.ask("Ok. are you ready?")
    }

    onResponse<Yes>{
            furhat.say("Good. Now place the cards on your and my forehead.")
            delay(5000)
            furhat.say("Can you look at me please, so I can read your cards.")
            GetCharactersImpl.saveCharacters { response ->
                furhat.run {
                    raise(SavedCharacters(response))
                }
            }
    }

    onResponse<No>{
//        TODO: add sentence to randomize text
            furhat.say("Okay, I will give you 10 more seconds.")
            delay(10000)
            furhat.ask("Ok. How about now?")
    }

    onEvent<SavedCharacters> {
        if (it.res) {
            goto(FirstPlayer)
        } else {
//            say something to get closer and remove picture stuff
            furhat.say("whooops there was a problem. Let's try again.")
            delay(2000)
            GetCharactersImpl.saveCharacters { response ->
                furhat.run {
                    raise(SavedCharacters(response))
                }
            }
        }
    }
}

val FirstPlayer: State = state(Parent){
    onEntry {
        furhat.say("Now the first player can start asking a question.")
        furhat.ask ("Do you want to start ${GameState.player1.realName}")
        //gazes at person to the left.
    }

    onResponse<Yes>{
        GameState.playerOnTurn = GameState.player1
        goto(TurnPlayer)
    }
    onResponse<No>{
        furhat.say("Then I will start.")
        goto(TurnFurhat) //start asking a question
    }
}
class SavedCharacters(val res: Boolean) : Event()

