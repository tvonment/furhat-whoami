package furhatos.app.furhatwhoami.shared

import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state

object GameState {
    var player1 = Player(0, "unknown", "unknown")
    var player2 = Player(0, "unknown", "unknown")
}

data class Player (
        var id: Int,
    var realName: String,
    var character: String
)