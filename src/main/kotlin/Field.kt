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

const val DEPTH = 8
const val HEIGHT = 8
const val WIDTH = 8

/** Игровое поле - переопределены операторы get/set для работы с эмуляцией трехмерного массива */
class BattleField {
    var field = Array<ObjectType>(DEPTH * WIDTH * HEIGHT, init = { ObjectType.EMPTY })

    operator fun get(x: Int, y: Int, d: Int): ObjectType {
        //todo: добавить контроль границ
        return field[d * WIDTH * HEIGHT + y * WIDTH + x]
    }

    operator fun set(x: Int, y: Int, d: Int, v: ObjectType) {
        //todo: добавить контроль границ и значений перечисления
        field[d * WIDTH * HEIGHT + y * WIDTH + x] = v
    }
}