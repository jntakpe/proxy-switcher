package com.github.jntakpe.proxyswitcher.filehandler

import com.github.jntakpe.proxyswitcher.utils.createIfMissing
import com.github.jntakpe.proxyswitcher.utils.keyExists
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.APPEND

class BashFileHandler(private val path: Path, private val create: Boolean = true) : FileHandler {

    init {
        createIfMissing(path, create)
    }

    override fun put(key: String, value: String) {
        val exportKey = toExportKey(key)
        if (keyExists(path, exportKey, "=")) {
            remove(key)
        }
        Files.write(path, listOf("$exportKey=$value"), APPEND)
    }

    override fun remove(key: String) {
        val updatedLines = Files.readAllLines(path).filter { !it.startsWith(toExportKey(key), true) }
        Files.write(path, updatedLines)
    }

    private fun toExportKey(key: String) = "export $key"

}