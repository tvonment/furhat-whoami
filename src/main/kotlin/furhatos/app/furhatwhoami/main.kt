package furhatos.app.furhatwhoami

import furhatos.app.furhatwhoami.flow.Init
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class FurhatwhoamiSkill : Skill() {
    override fun start() {
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
