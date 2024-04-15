package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.flow.kotlin.*
//import furhatos.flow.kotlin.onResponse
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
//import okhttp3.internal.wait
//import java.awt.SystemColor.text


val Greeting: State = state(Parent) {
    onEntry {
        furhat.say("Hi there.")
        delay(100)
        goto(YourName)
    }
}

val YourName: State = state(Parent){
    onEntry{
        furhat.ask("What is your name dude?")
    }
    onResponse {
        //extract name from answer
        val response = (it.text)
        val pattern=Regex("my name is (\\w+)")
        val matchResult = pattern.find(response)

        //save name of first payer to nameID1
        val nameID1 = matchResult?.groupValues?.get(1)
        furhat.say("Nice to meet you $nameID1")
        goto(WantToPlay) //change to Want to play
    }
//    onEvent {
//        furhat.ask("And what is your name?")
//    }
//    onResponse {
//        //extract name from answer
//        val response = (it.text)
//        val pattern = Regex("my name is (\\w+)")
//        val matchResult = pattern.find(response)
//
//        //save name of second player to nameID2
//        val nameID2 = matchResult?.groupValues?.get(1)
//        furhat.say("Nice to meet you too $nameID2")
//        goto(WantToPlay)
//    }
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










