package com.github.jntakpe.proxyswitcher.filehandler

import com.github.jntakpe.proxyswitcher.utils.createIfMissing
import com.github.jntakpe.proxyswitcher.utils.keyExists
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.APPEND

class PropertyFileHandler(private val path: Path, create: Boolean = true) : FileHandler {

    init {
        createIfMissing(path, create)
    }

    override fun put(key: String, value: String) {
        if (keyExists(path, key, "=")) {
            remove(key)
        }
        Files.write(path, listOf("$key=$value"), APPEND)
    }

    override fun remove(key: String) {
        val updatedLines = Files.readAllLines(path).filter { !it.startsWith(key, true) }
        Files.write(path, updatedLines)
    }

}