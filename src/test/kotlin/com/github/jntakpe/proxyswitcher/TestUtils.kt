package com.github.jntakpe.proxyswitcher

import org.apache.commons.io.FileUtils
import java.nio.file.Files
import java.nio.file.Paths

internal fun cleanUpTmpDir() {
    FileUtils.deleteQuietly(tmpDirPath().toFile())
    Files.createDirectory(tmpDirPath())
}

internal fun tmpDirPath() = Paths.get("src", "test", "resources", "tmp")