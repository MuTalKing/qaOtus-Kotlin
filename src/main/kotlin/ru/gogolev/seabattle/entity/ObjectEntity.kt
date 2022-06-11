package ru.gogolev.seabattle.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ru.gogolev.seabattle.objects.ObjectType

object Objects : IntIdTable() {
    val x = integer("x")
    val y = integer("y")
    val z = integer("z")
    val objectType = enumeration("objectType", ObjectType::class)
}

class Object(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Object>(Objects)

    var x by Objects.x
    var y by Objects.y
    var z by Objects.z
    var objectType by Objects.objectType

    override fun toString(): String {
        return "Object(x=$x, y=$y, z=$z, type=$objectType)"
    }
}