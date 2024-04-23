package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.app.furhatwhoami.shared.GameState
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
//import furhatos.flow.kotlin.onResponse
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.records.Location

//import okhttp3.internal.wait
//import java.awt.SystemColor.text


val Greeting: State = state(Parent) {
    onEntry {
        furhat.say("Hi there.")
        delay(100)
        goto(YourName)
    }
}

val YourName: State = state(Parent) {
    onEntry {
        furhat.ask{
            +"What is your name?"
            +glance(Location.LEFT) //looks at player on the left
        }
    }
    onResponse {
        //extract name from answer
        val response = (it.text)
        val pattern = Regex("my name is (\\w+)")
        val matchResult = pattern.find(response)

        //save name of first payer to nameID1
        val nameID1 = matchResult?.groupValues?.get(1)
        GameState.player1.realName = nameID1.toString()
        furhat.say("Nice to meet you ${GameState.player1.realName}")
        goto(YourName2) //change to Want to play // CHANG TO YOUR NAME 2

    }
}
val YourName2: State = state(Parent) {
    onEntry {
        furhat.ask{
            +"And what is your name?"
            +glance(Location.RIGHT)// gaze to the 2nd person
    }
    }
    onResponse {
        //extract name from second answer
//        TODO: only my name is works now, should be fixed later
        val response = (it.text)
        val pattern = Regex("my name is (\\w+)") //add other variables
        val matchResult = pattern.find(response)

        //save name of first payer to nameID1
        val nameID2 = matchResult?.groupValues?.get(1)
        GameState.player2.realName = nameID2.toString()
        furhat.say {
            "It's a pleasure to meet you ${GameState.player2.realName}"
            +glance(Location.RIGHT)
            +Gestures.Smile
        }
        goto(WantToPlay) //change to Want to play
    }
    onNoResponse {
        furhat.say("I'm sorry I didn't hear you")
        reentry()
    }
}


val WantToPlay: State = state(Parent){

    onEntry {
        furhat.ask("Do you two want to play a game of who am I?")
    }

    onResponse<Yes>{
            furhat.say("Nice! Let's start")
            goto(CheckKnow)
        }

    onResponse<No> {
            furhat.say("Ok, too bad.")
            //wait? what to do here
        }
}

val CheckKnow: State = state(Parent){
    onEntry{
        furhat.ask("Do you know the game?")
    }
    onResponse<Yes>{
        furhat.say("Ok. Let's start.")
        goto(BeginGame)
    }
    onResponse<No>{
        furhat.say("Ok. Then I will explain the game")
        goto(ExplainGame)
    }

}

val ExplainGame: State = state(Parent){
    onEntry {
        furhat.say("Who am I is a game where every user has to find out " +
                "which character is assigned to them. And you find out by asking " +
                "questions that can be answered with yes or no.")
        goto(BeginGame)
    }
}










