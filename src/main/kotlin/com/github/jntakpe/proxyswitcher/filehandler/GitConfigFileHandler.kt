package com.github.jntakpe.proxyswitcher.filehandler

import com.github.jntakpe.proxyswitcher.utils.createIfMissing
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.APPEND

class GitConfigFileHandler(private val path: Path, create: Boolean = true) : FileHandler {

    init {
        createIfMissing(path, create)
    }

    override fun put(key: String, value: String) {
        if (keyExists(key)) {
            remove(key)
        }
        Files.write(path, listOf(protocolPrefix(key), proxyPrefix(key, value)), APPEND)
    }

    override fun remove(key: String) {
        val lines = Files.readAllLines(path)
        val protocolIndex = lines.indexOfFirst { it.equals(protocolPrefix(key), true) }
        if (protocolIndex != -1) {
            lines.apply(removeTwoLinesAtIndex(protocolIndex)).let { Files.write(path, it) }
        }
    }

    private fun keyExists(key: String) = Files.readAllLines(path).map { it.trim().toLowerCase() }.contains(protocolPrefix(key))

    private fun removeTwoLinesAtIndex(index: Int): MutableList<String>.() -> Unit {
        return {
            removeAt(index)
            removeAt(index)
        }
    }

    private fun protocolPrefix(key: String) = "[${key.toLowerCase()}]"

    private fun proxyPrefix(key: String, value: String) = "    proxy = $key://$value"
}