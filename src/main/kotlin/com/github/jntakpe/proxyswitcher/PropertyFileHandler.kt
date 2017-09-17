package com.github.jntakpe.proxyswitcher

import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.APPEND

class PropertyFileHandler(private val path: Path, private val create: Boolean = true) {

    init {
        createIfMissing()
    }

    fun put(key: String, value: String) {
        if (keyExists(key)) {
            remove(key)
        }
        Files.write(path, listOf("$key=$value"), APPEND)
    }

    fun remove(key: String) {
        val updatedLines = Files.readAllLines(path).filter { !it.startsWith(key) }
        Files.write(path, updatedLines)
    }

    private fun createIfMissing() {
        val exists = Files.exists(path)
        if (create && !exists) {
            Files.createFile(path)
        } else if (!exists) {
            throw FileNotFoundException("File ${path.toAbsolutePath()} doesn't exist")
        }
    }

    private fun keyExists(key: String) = Files.readAllLines(path)
            .map { it.substringBefore("=") }
            .map { it.trim() }
            .contains(key)

}