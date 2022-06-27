package ru.gogolev.seabattle.utils

import java.nio.file.Files
import kotlin.io.path.Path

class DbFileUtils {
    private val main = Path("db/seaBattle.db")
    private val backup = Path("db/seaBattle-backup.db")

    fun backupData() {
        if (Files.exists(main)) {
            Files.deleteIfExists(backup)
            Files.copy(main, backup)
        }
    }

    fun restoreData() {
        if (Files.exists(backup)) {
            Files.deleteIfExists(main)
            Files.copy(backup, main)
        }
    }

    fun createDirectory() {
        val dbPath = Path("db")
        if (!Files.isDirectory(dbPath)) Files.createDirectory(dbPath)
    }
}