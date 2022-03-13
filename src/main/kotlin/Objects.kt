abstract class GameObject(var x: Int, var y: Int, var z: Int) {
    abstract fun interact(x: Int, y: Int, z: Int, gameLoop: GameLoop)
}

class MineObject(x: Int, y: Int, z: Int) : GameObject(x = x, y = y, z = z) {
    override fun interact(x: Int, y: Int, z: Int, gameLoop: GameLoop) {
        //todo: implement interaction with mine
    }
}

class HelpObject(x: Int, y: Int, z: Int) : GameObject(x = x, y = y, z = z) {
    override fun interact(x: Int, y: Int, z: Int, gameLoop: GameLoop) {
        //todo: implement interaction with help request
    }
}

open class StaticShipObject(x: Int, y: Int, z: Int, val len: Int, val direction: Direction) : GameObject(x = x, y = y, z = z) {
    override fun interact(x: Int, y: Int, z: Int, gameLoop: GameLoop) {
        //todo: implement interaction with static ship
    }
}

class MovingShipObject(x: Int, y: Int, z: Int, len: Int, direction: Direction) :
    StaticShipObject(x, y, z, len, direction) {
    override fun interact(x: Int, y: Int, z: Int, gameLoop: GameLoop) {
        //todo: implement interaction with moving ship
    }
}