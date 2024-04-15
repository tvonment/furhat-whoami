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
//        delay(5000)
        furhat.ask("Ok. are you ready?")
    }

    onResponse<Yes>{
//            furhat.say("Good. Now place the cards on your forehead and lay mine in front of me. But don't block my camera please.")
//            delay(5000)
//            furhat.say("Can you look at me please, so I can read your cards.")
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
            furhat.say("whooops")
        }
    }
}

val FirstPlayer: State = state(Parent){
    onEntry {
        furhat.say("Now the first player can start asking a question.")
        furhat.ask ("Do you want to start ${GameState.player1.realName}")
        //gazes at person to the left.
    }
    onResponse<Yes> {
        furhat.ask("Well, take it away. Ask the first question.")
    }
    onResponse<No>{
        furhat.say("Then I will start.")
        //start asking a question
        goto(QuestionFurhat)

    }

    onResponse {

        OpenAIServiceImpl.sendMessage("player1", it.text, ) { response ->
            furhat.run {
                raise(HasAnswer(response))
            }
        }
    }

    onEvent<HasAnswer> {
        furhat.say(it.answer)
//        TODO: wait for other response
//        TODO: if yes --> go to another round, if no --> next turn (depending on the player)
    }
}

val QuestionFurhat: State = state(Parent) {
    onEntry {
        OpenAIServiceImpl.sendMessage("furhat", "your turn") {
            response ->
            furhat.run {
                raise(HasAnswer(response))
            }
        }
    }

    onEvent<GotQuestion> {
        furhat.ask(it.question)
    }

    onResponse<No> {
            furhat.say("It's your turn ${GameState.player1.realName}")
        }
    onResponse<Yes> {
            reentry()
    }
}

class HasAnswer(val answer: String) : Event()
class GotQuestion(val question: String) : Event()
class SavedCharacters(val res: Boolean) : Event()
