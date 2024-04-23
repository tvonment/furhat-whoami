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
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val EndGameLose : State = state(Parent){
    onEntry {
        //if player 1 or 2 wins
        furhat.say{
            random {
                +"Congratulations winner"
                +"Well, well, that was quick"
                +"Ok. You deserve it, congrats"
                +"Hmpf, I almost had it"
                +"Ah, come on. This is unfair, your character was way easier then mine."
            }
        }
    }

}

val EndGameWin : State = state(Parent){
    onEntry {
        //if furhat wins
        furhat.ask{
            random {
                +"I guess we have a winner then"
                +"I won! Yes!"
                +"Easy peasy, I won this game."
                +"Thank you for being such good competitors"
            }
        }
//        onResponse{
//            if it.text contains ("congratulations"){
//                furhat.say("thank you so much!")
                //end game
//            }
//            else{
                furhat.say("it was nice playing with you")
                //end game
//            }

        }
    }
//}