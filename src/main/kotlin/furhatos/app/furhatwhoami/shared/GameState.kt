package furhatos.app.furhatwhoami.shared

import furhatos.app.furhatwhoami.services.ChatHistoryItem
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state

object GameState {
    var player1 = Player(0, "unknown", "unknown")
    var player2 = Player(0, "unknown", "unknown")
<<<<<<< HEAD
    var openAiHistory = mutableListOf<ChatHistoryItem>()
=======
    var characters = arrayOf("King Kong", "Donald Duck", "Micky Mouse", "Aladin", "Simba", "Pumbaa", "Baloo", "Elsa", "Belle", "Mulan")
>>>>>>> 1eaee51908c6cc170cc8fe949d30bf59bdccdaed
}

data class Player (
        var id: Int,
    var realName: String,
    var character: String
)