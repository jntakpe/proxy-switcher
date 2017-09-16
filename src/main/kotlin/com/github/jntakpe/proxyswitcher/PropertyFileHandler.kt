package com.github.jntakpe.proxyswitcher

import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.APPEND

class PropertyFileHandler(private val path: Path, val create: Boolean = true) {

    init {
        createIfMissing()
    }

    fun put(key: String, value: String) = Files.write(path, listOf("$key=$value"), APPEND)

    private fun createIfMissing() {
        val exists = Files.exists(path)
        if (create && !exists) {
            Files.createFile(path)
        } else if (!exists) {
            throw FileNotFoundException("File $path doesn't exist")
        }
    }

}