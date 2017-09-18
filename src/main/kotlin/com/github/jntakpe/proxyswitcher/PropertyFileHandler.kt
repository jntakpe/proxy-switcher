package com.github.jntakpe.proxyswitcher

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.APPEND

class PropertyFileHandler(private val path: Path, private val create: Boolean = true) : FileHandler {

    init {
        createIfMissing(path, create)
    }

    override fun put(key: String, value: String) {
        if (keyExists(key)) {
            remove(key)
        }
        Files.write(path, listOf("$key=$value"), APPEND)
    }

    override fun remove(key: String) {
        val updatedLines = Files.readAllLines(path).filter { !it.startsWith(key) }
        Files.write(path, updatedLines)
    }

    private fun keyExists(key: String) = Files.readAllLines(path)
            .map { it.substringBefore("=") }
            .map { it.trim() }
            .contains(key)

}