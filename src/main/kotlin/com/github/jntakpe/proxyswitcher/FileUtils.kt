package com.github.jntakpe.proxyswitcher

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