import kotlin.random.Random
import kotlin.reflect.KProperty


const val MINES_COUNT = 6
const val HELP_COUNT = 6
const val SHIPS_LEN_1_COUNT = 6
const val SHIPS_LEN_2_COUNT = 5
const val SHIPS_LEN_3_COUNT = 4
const val SHIPS_LEN_4_COUNT = 3


class BattleFieldDelegate(val gameObjects: MutableList<GameObject>) {
    var battleField: BattleField? = null

    // возвращает true, если есть соседи
    fun checkNeighbors(d: Int, y: Int, x: Int): Boolean {
        var sum = 0
        for (dd in (if (d > 1) d - 1 else d)..(if (d < DEPTH - 1) d + 1 else d)) {
            for (dy in (if (y > 1) y - 1 else y)..(if (y < HEIGHT - 1) y + 1 else y)) {
                for (dx in (if (x > 1) x - 1 else x)..(if (x < WIDTH - 1) x + 1 else x)) {
                    if (battleField!![dx, dy, dd] != ObjectType.EMPTY) sum++
                }
            }
        }
        return sum != 0
    }

    fun generateRandomPosition(): Triple<Int, Int, Int> {
        val x = Random.nextInt(0, WIDTH)
        val y = Random.nextInt(0, HEIGHT)
        val z = Random.nextInt(0, DEPTH)
        return Triple(x, y, z)
    }

    fun propagateMines() {
        var mine = 0
        while (mine < MINES_COUNT) {
            val (x, y, z) = generateRandomPosition()
            if (!checkNeighbors(x, y, z)) {
                gameObjects.add(MineObject(x, y, z))
                battleField!![x, y, z] = ObjectType.MINE
                mine++
            }
        }
    }

    fun propogateHelp() {
        var mine = 0
        while (mine < HELP_COUNT) {
            val (x, y, z) = generateRandomPosition()
            if (!checkNeighbors(x, y, z)) {
                gameObjects.add(HelpObject(x, y, z))
                battleField!![x, y, z] = ObjectType.HELP
                mine++
            }
        }
    }

    private fun nextPos(x: Int, y: Int, z: Int, direction: Direction): Triple<Int, Int, Int>? {
        return when (direction) {
            Direction.LEFT -> if (x > 0) Triple(x - 1, y, z) else null
            Direction.RIGHT -> if (x < WIDTH - 1) Triple(x + 1, y, z) else null
            Direction.UP -> if (y > 0) Triple(x, y - 1, z) else null
            Direction.DOWN -> if (y < HEIGHT - 1) Triple(x, y + 1, z) else null
            Direction.TO_THE_SURFACE -> if (z > 0) Triple(x, y, z - 1) else null
            Direction.DEEP_INTO -> if (z < DEPTH - 1) Triple(x, y, z + 1) else null
        }
    }

    fun propogateShipToMap(x: Int, y: Int, z: Int, len: Int, direction: Direction, type: ObjectType) {
        var cx = x
        var cy = y
        var cz = z
        for (i in 1..len) {
            battleField!![cx, cy, cz] = type
            val l = nextPos(x, y, z, direction)
            l?.let {
                cx = l.first
                cy = l.second
                cz = l.third
            }
        }
    }

    private fun canPropogateShip(x: Int, y: Int, z: Int, len: Int, direction: Direction): Boolean {
        //текущее положение точки заполнения
        var cx = x
        var cy = y
        var cz = z
        for (i in 1..len) {
            if (checkNeighbors(cx, cy, cz)) {
                return false
            }
            val l = nextPos(cx, cy, cz, direction) ?: return false                  //попали в стенку
            //продолжаем заполнение
            cx = l.first
            cy = l.second
            cz = l.third
        }
        return true
    }

    fun placeShip(len: Int, type: ObjectType) {
        while (true) {
            val direction = Direction.values()[Random.nextInt(Direction.values().size)]
            val (x, y, z) = generateRandomPosition()
            if (canPropogateShip(x, y, z, len, direction)) {
                //сохранить корабль и разметить точки на поле
                propogateShipToMap(x, y, z, len, direction, type)
                gameObjects.add(StaticShipObject(x, y, z, len, direction))
                break
            }
        }
    }

    fun propogateShips() {
        for (i in 1..SHIPS_LEN_4_COUNT) placeShip(4, ObjectType.STATIC_SHIP)
        for (i in 1..SHIPS_LEN_3_COUNT) placeShip(3, ObjectType.STATIC_SHIP)
        for (i in 1..SHIPS_LEN_2_COUNT) placeShip(2, ObjectType.STATIC_SHIP)
        for (i in 1..SHIPS_LEN_1_COUNT) placeShip(1, ObjectType.STATIC_SHIP)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): BattleField {
        if (battleField == null) {
            //заполнить поле случайным расположением
            battleField = BattleField()
            //заполняем объектами
            println("Mines")
            propagateMines()
            println("Helps")
            propogateHelp()
            println("Ships")
            propogateShips()
            println("Battle field is filled")
        }
        return battleField!!
    }
}