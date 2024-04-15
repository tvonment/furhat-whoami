package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
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
            furhat.say("Good. Now place the cards on your forehead and lay mine in front of me. But don't block my camera please.")
            delay(1000)
            goto(FirstPlayer)
    }

    onResponse<No>{
            furhat.say("Okay, I will give you 10 more seconds.")
            delay(10000)
            furhat.say("Ok. Now place the cards on your forehead and lay mine in front of me. But don't block my camera please.")
            delay(5000)
    }
}

val FirstPlayer: State = state(Parent){
    onEntry {
        furhat.say("Now the first player can start asking a question.")
        furhat.ask ("Do you want to start ") //here I am
        //gazes at person to the left.
    }
    onResponse<Yes>{
        furhat.say("Well, take it away. Ask the first question.")
        //what to do next??
//        goto(Openai) ??
    }
    onResponse<No>{
        furhat.say("Then I will start.")
        //start asking a question
//        goto(Openai) ??
    }
}
