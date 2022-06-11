package ru.gogolev.test.seabattle

import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import ru.gogolev.seabattle.fields.BattleField
import ru.gogolev.seabattle.fields.Point
import ru.gogolev.seabattle.objects.ObjectType

class FillingObjectTest : FreeSpec() {
    init {
        "Check filling an Object" - {
            val battleField = BattleField()
            forAll(
                row(0, 0, 0),
                row(4, 5, 3),
                row(2, 4, 2),
                row(7, 7, 7)
            ) { x, y, z ->
                "The object[$x, $y, $z] is available"{
                    battleField.isAvailablePoint(Point(x, y, z)) shouldBe true
                }

                "When the object[$x, $y, $z] is filled"{
                    battleField[x, y, z] = ObjectType.MINE
                }

                "Then the object[$x, $y, $z] is unavailable"{
                    battleField.isAvailablePoint(Point(x, y, z)) shouldBe false
                }
            }
        }
    }
}
