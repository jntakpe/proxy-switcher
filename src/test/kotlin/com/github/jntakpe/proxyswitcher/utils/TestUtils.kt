package com.github.jntakpe.proxyswitcher.utils

import org.apache.commons.io.FileUtils
import java.nio.file.Files
import java.nio.file.Paths

internal fun cleanUpTmpDir() {
    FileUtils.deleteDirectory(tmpDirPath().toFile())
    Files.createDirectory(tmpDirPath())
}

internal fun resourcesDirPath() = Paths.get("src", "test", "resources")

internal fun tmpDirPath() = resourcesDirPath().resolve("tmp")