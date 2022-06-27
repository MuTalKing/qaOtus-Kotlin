package ru.gogolev.seabattle.utils

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.gogolev.seabattle.configuration.DbConnect
import ru.gogolev.seabattle.entity.Object
import ru.gogolev.seabattle.entity.Objects
import ru.gogolev.seabattle.objects.ObjectType

class DbUtils {
    init {
        DbConnect.connect
    }

    fun createDb(){
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Objects)
        }
    }

    fun insertObjectType(x: Int, y: Int, z: Int, objectType: ObjectType){
        transaction {
            Object.new {
                this.x = x
                this.y = y
                this.z = z
                this.objectType = objectType
            }
        }
    }

    fun selectAllObjectTypes(): ArrayList<Object> {
        val arrayList: ArrayList<Object> = arrayListOf()
        transaction {
            Object.all().forEach {
                arrayList += it
            }
        }
        return arrayList
    }
}