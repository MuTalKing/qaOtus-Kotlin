import kotlin.concurrent.timer
import kotlin.random.Random
import kotlin.reflect.KProperty

class GameLoop {
    val gameObjects = mutableListOf<GameObject>()
    val battleField by BattleFieldDelegate(gameObjects)

    fun start() {
        battleField.render()
    }
}