package furhatos.app.furhatwhoami.flow

//import furhatos.app.furhatwhoami.flow.main.Idle
import furhatos.app.furhatwhoami.flow.main.BeginGame
import furhatos.app.furhatwhoami.flow.main.Greeting
import furhatos.app.furhatwhoami.flow.main.Openai2
import furhatos.app.furhatwhoami.flow.main.TurnPlayer
//import furhatos.app.furhatwhoami.flow.main.Openai
import furhatos.app.furhatwhoami.setting.DISTANCE_TO_ENGAGE
import furhatos.app.furhatwhoami.setting.MAX_NUMBER_OF_USERS
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users
import javax.swing.text.html.HTML.Tag.HEAD

val Init: State = state {
    init {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)
    }
    onEntry {
        /** start interaction */
        when {
            furhat.isVirtual() -> goto(Greeting) // Convenient to bypass the need for user when running Virtual Furhat
        }
        goto(Greeting)
    }
}
