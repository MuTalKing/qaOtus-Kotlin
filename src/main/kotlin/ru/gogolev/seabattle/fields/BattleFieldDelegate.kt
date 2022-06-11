package ru.gogolev.seabattle.fields

import ru.gogolev.seabattle.configuration.*
import ru.gogolev.seabattle.objects.*
import ru.gogolev.seabattle.utils.DbFileUtils
import kotlin.random.Random
import kotlin.reflect.KProperty

class BattleFieldDelegate(val gameObjects: MutableList<GameObject>) {
    var battleField: BattleField? = null

    // возвращает true, если есть соседи
    fun checkNeighbors(point: Point): Boolean {
        val points = mutableListOf<Point>()
        var neighbors = false
        listOf(point.x - 1, point.x, point.x + 1).forEach { first ->
            listOf(point.y - 1, point.y, point.y + 1).forEach { second ->
                listOf(point.z - 1, point.z, point.z + 1).forEach { third ->
                    if (first in 0 until WIDTH && second in 0 until DEPTH && third in 0 until HEIGHT) {
                        points.add(Point(first, second, third))
                    }
                }
            }
        }
        for (point in points) {
            if (!battleField!!.isAvailablePoint(point)) {
                neighbors = true
                break
            }
        }
        return neighbors
    }

    fun generateRandomPosition(): Point {
        val x = Random.nextInt(0, WIDTH)
        val y = Random.nextInt(0, DEPTH)
        val z = Random.nextInt(0, HEIGHT)
        return Point(x, y, z)
    }

    fun propagateMines() {
        var mine = 0
        while (mine < MINES_COUNT) {
            val (x, y, z) = generateRandomPosition()
            if (!checkNeighbors(Point(x, y, z))) {
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
            if (!checkNeighbors(Point(x, y, z))) {
                gameObjects.add(HelpObject(x, y, z))
                battleField!![x, y, z] = ObjectType.HELP
                mine++
            }
        }
    }

    private fun nextPos(point: Point, direction: Direction): Point? {
        val (x, y, z) = point
        return when (direction) {
            Direction.LEFT -> if (x > 0) Point(x - 1, y, z) else null
            Direction.RIGHT -> if (x < WIDTH - 1) Point(x + 1, y, z) else null
            Direction.BACK -> if (y > 0) Point(x, y - 1, z) else null
            Direction.FORWARD -> if (y < DEPTH - 1) Point(x, y + 1, z) else null
            Direction.UP -> if (z > 0) Point(x, y, z - 1) else null
            Direction.DOWN -> if (z < HEIGHT - 1) Point(x, y, z + 1) else null
        }
    }

    fun propogateShipToMap(x: Int, y: Int, z: Int, len: Int, direction: Direction, type: ObjectType) {
        var cx = x
        var cy = y
        var cz = z
        for (i in 1..len) {
            battleField!![cx, cy, cz] = type
            val l = nextPos(Point(cx, cy, cz), direction)
            l?.let {
                cx = l.x
                cy = l.y
                cz = l.z
            }
        }
    }

    private fun canPropogateShip(x: Int, y: Int, z: Int, len: Int, direction: Direction): Boolean {
        //текущее положение точки заполнения
        var cx = x
        var cy = y
        var cz = z
        for (i in 1..len) {
            if (checkNeighbors(Point(cx, cy, cz))) {
                return false
            }
            val l = nextPos(Point(cx, cy, cz), direction) ?: return false                  //попали в стенку
            //продолжаем заполнение
            cx = l.x
            cy = l.y
            cz = l.z
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
        val dbFileUtils = DbFileUtils()
        if (battleField == null) {
            //заполнить поле случайным расположением
            battleField = BattleField()
            //заполняем объектами
            if(NEW_GAME) {
                dbFileUtils.createDirectory()
                println("Mines")
                propagateMines()
                println("Helps")
                propogateHelp()
                println("Ships")
                propogateShips()
                println("Battle field is filled")
                dbFileUtils.backupData()
            }
            else{
                battleField!!.prepareBattleField()
            }
        }
        return battleField!!
    }
}