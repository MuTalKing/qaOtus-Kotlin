package ru.gogolev.seabattle

import ru.gogolev.seabattle.fields.BattleFieldDelegate
import ru.gogolev.seabattle.fields.render
import ru.gogolev.seabattle.objects.GameObject

class GameLoop {
    val gameObjects = mutableListOf<GameObject>()
    val battleField by BattleFieldDelegate(gameObjects)

    fun start() {
        battleField.render()
    }
}