package com.github.jntakpe.proxyswitcher.utils

import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path


internal fun createIfMissing(path: Path, create: Boolean) {
    val exists = Files.exists(path)
    if (create && !exists) {
        Files.createFile(path)
    } else if (!exists) {
        throw FileNotFoundException("File ${path.toAbsolutePath()} doesn't exist")
    }
}

internal fun keyExists(path: Path, key: String, delimiter: String): Boolean {
    return Files.readAllLines(path)
            .map { it.substringBefore(delimiter) }
            .map { it.trim() }
            .any { it.equals(key, true) }
}