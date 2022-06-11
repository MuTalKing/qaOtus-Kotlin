package ru.gogolev.seabattle.fields

import ru.gogolev.seabattle.configuration.DEPTH
import ru.gogolev.seabattle.configuration.HEIGHT
import ru.gogolev.seabattle.configuration.WIDTH
import ru.gogolev.seabattle.configuration.NEW_GAME
import ru.gogolev.seabattle.objects.ObjectType
import ru.gogolev.seabattle.utils.DbUtils

fun BattleField.renderLine(y: Int, z: Int): String {
    var line = ""
    for (x in 0 until WIDTH) {
        line += when (this[x, y, z]) {
            ObjectType.MINE -> "@"
            ObjectType.HELP -> "!"
            ObjectType.STATIC_SHIP -> "*"
            ObjectType.MOVING_SHIP -> ">"
            else -> " "
        }
    }
    return line
}

fun BattleField.render() {
    // группировка по 4
    for (zr in 0 until (DEPTH + 3) / 4) {
        for (y in 0 until HEIGHT) {
            var line = ""
            for (zd in 0..3) {
                line += renderLine(y, zr * 4 + zd)
                if (zd != 3) line += "  |  "
            }
            println(line)
        }
        println("================================================")
    }
}



/** Игровое поле - переопределены операторы get/set для работы с эмуляцией трехмерного массива */
class BattleField(depth: Int = DEPTH, width: Int = WIDTH, height: Int = HEIGHT) {
    private val dbUtils = DbUtils()
    private var field = Array(depth * width * height, init = { ObjectType.EMPTY})

    fun prepareBattleField(){
        val objects = dbUtils.selectAllObjectTypes()
        objects.forEach {
            field[calculatePosition(Point(it.x, it.y, it.z))] = it.objectType
        }
    }

    operator fun get(x: Int, y: Int, z: Int): ObjectType {
        if (checkBoundaries(Point(x, y, z))) {
            return field[calculatePosition(Point(x, y, z))]
        } else throw IllegalArgumentException("get incorrect position: $x,$y,$z")
    }

    operator fun set(x: Int, y: Int, z: Int, v: ObjectType) {
        if (checkBoundaries(Point(x, y, z)) && isAvailablePoint(Point(x, y, z))) {
            field[calculatePosition(Point(x, y, z))] = v
            if(NEW_GAME) {
                dbUtils.createDb()
                dbUtils.insertObjectType(x, y, z, v)
            }
        } else throw IllegalArgumentException("set incorrect position: $x,$y,$z $v")
    }

    private fun checkBoundaries(point: Point): Boolean {
        return (point.x in 0 until WIDTH) && (point.y in 0 until DEPTH) && (point.z in 0 until HEIGHT)
    }

    private fun calculatePosition(p: Point) = ((p.x * DEPTH * HEIGHT) + (p.y * WIDTH) + p.z)

    fun isAvailablePoint(p: Point): Boolean = get(p.x, p.y, p.z) == ObjectType.EMPTY
}