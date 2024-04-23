package furhatos.app.furhatwhoami.shared

import furhatos.app.furhatwhoami.services.ChatHistoryItem

object GameState {
    var currentQuestion = ""
    var player1 = Player(0, "", "")
    var player2 = Player(0, "", "")
    var playerOnTurn: Player = player1
    var characters = arrayOf("King Kong", "Donald Duck", "Micky Mouse", "Aladin", "Simba", "Pumbaa", "Baloo", "Elsa", "Belle", "Mulan")
    var openAiHistory = mutableListOf<ChatHistoryItem>()
    var thingsyouknow = mutableListOf<String>()
}

data class Player (
        var id: Int,
    var realName: String,
    var character: String
)