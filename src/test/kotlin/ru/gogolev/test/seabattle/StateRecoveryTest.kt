package ru.gogolev.test.seabattle

import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import ru.gogolev.seabattle.fields.BattleFieldDelegate
import ru.gogolev.seabattle.fields.Point
import ru.gogolev.seabattle.objects.GameObject
import ru.gogolev.seabattle.objects.ObjectType

class StateRecoveryTest: FreeSpec() {
    init{
        "Check state recovery" - {
            val gameObjects = mutableListOf<GameObject>()
            val battleField by BattleFieldDelegate(gameObjects, true)
            forAll(
                row(0, 0, 0)
            ) { x, y, z ->
                "The object[$x, $y, $z] is available" {
                    battleField.isAvailablePoint(Point(x, y, z)) shouldBe true
                }

                "When the object[$x, $y, $z] is filled" {
                    battleField[x, y, z] = ObjectType.MINE
                }

                "Then the object[$x, $y, $z] is unavailable" {
                    battleField.isAvailablePoint(Point(x, y, z)) shouldBe false
                }

                "And state recovery is working"{
                    val battleField2 by BattleFieldDelegate(gameObjects, false)
                    battleField2[x, y, z] shouldBe ObjectType.MINE
                }
            }
        }
    }
}