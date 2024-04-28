package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.app.furhatwhoami.shared.GameState
import furhatos.event.Event
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
        furhat.say("Hi there. Nice to meet you!")
        delay(1000)
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
        var name = ""
        if (it.text.contains("my name")) {
            val pattern = Regex("my name is (\\w+)")
            val matchResult = pattern.find(response)

            //save name of first payer to nameID1
            name = matchResult?.groupValues?.get(1).toString()
        } else if (it.text.contains("I am")) {
            val pattern = Regex("I am (\\w+)")
            val matchResult = pattern.find(response)

            //save name of first payer to nameID1
            name = matchResult?.groupValues?.get(1).toString()
        } else {
            name = it.text
        }

        GameState.player1.realName = name
        furhat.say("Nice to meet you ${GameState.player1.realName}")
        goto(YourName2) //change to Want to play // CHANG TO YOUR NAME 2

    }
}
val YourName2: State = state(Parent) {
    onEntry {
        furhat.attend(Location.RIGHT)
        furhat.ask{
            +"And what is your name?"
            +glance(Location.RIGHT)// gaze to the 2nd person
        }
    }
    onResponse {
        //extract name from second answer
        val response = (it.text)
        var name = ""
        if (it.text.contains("my name")) {
            val pattern = Regex("my name is (\\w+)")
            val matchResult = pattern.find(response)

            //save name of first payer to nameID1
            name = matchResult?.groupValues?.get(1).toString()
        } else if (it.text.contains("I am")) {
            val pattern = Regex("I am (\\w+)")
            val matchResult = pattern.find(response)

            //save name of first payer to nameID1
            name = matchResult?.groupValues?.get(1).toString()
        } else {
            name = it.text
        }

        GameState.player2.realName = name
        furhat.say ("It's a pleasure to meet you ${GameState.player2.realName}")

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
            furhat.say("Nice!")
            goto(CheckKnow)
        }

    onResponse<No> {
            furhat.say("Ok, too bad.")
            delay(1000)
            furhat.ask("how about now?")
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
        //goto(QuestionGameRules)
    }


}
/*
val QuestionGameRules: State = state(Parent) {
    onEntry {
        furhat.ask("Do you have questions?")
    }
    onResponse<No> {
        furhat.say("Ok. Let's start.")
        goto(BeginGame)
    }

    onResponse<Yes> {
        furhat.ask("Ok. What is your question?")
    }

    onResponse {
        OpenAIRulesServiceImpl.sendMessage(it.text) { response ->
            furhat.run {
                raise(GotFurhatAnswer(response))
            }
        }
    }

    onEvent<GotFurhatRuleAnswer>{
        furhat.say(it.answer)
        reentry()
    }
}

class GotFurhatRuleAnswer(val answer: String) : Event()



*/






