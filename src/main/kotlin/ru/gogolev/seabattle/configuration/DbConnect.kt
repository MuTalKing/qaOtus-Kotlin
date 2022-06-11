package ru.gogolev.seabattle.configuration

import org.jetbrains.exposed.sql.Database

object DbConnect {
    val connect by lazy {
        Database.connect("jdbc:sqlite:db/seaBattle.db", driver = "org.sqlite.JDBC")
    }
}