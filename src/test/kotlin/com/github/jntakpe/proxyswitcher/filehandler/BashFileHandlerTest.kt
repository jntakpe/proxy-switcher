package com.github.jntakpe.proxyswitcher.filehandler

import com.github.jntakpe.proxyswitcher.utils.cleanUpTmpDir
import com.github.jntakpe.proxyswitcher.utils.tmpDirPath
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.nio.file.Files

internal class BashFileHandlerTest {

    @BeforeEach
    fun beforeEach() {
        cleanUpTmpDir()
        createEmptyBashFile()
        createNotEmptyBashFile()
    }

    @AfterEach
    fun afterEach() {
        cleanUpTmpDir()
    }

    @Test
    fun `init should create new file`() {
        val path = tmpDirPath().resolve(".bash_profile")
        BashFileHandler(path)
        assertThat(path).exists()
    }

    @Test
    fun `init should fail if file does not exist`() {
        val path = tmpDirPath().resolve(".unknown_bash_profile")
        assertThat(catchThrowable { BashFileHandler(path, false) }).isInstanceOf(FileNotFoundException::class.java)
    }

    @Test
    fun `put should add entry to empty file`() {
        val path = tmpDirPath().resolve(".empty_bash_profile")
        val key = "newline"
        val value = "some value"
        BashFileHandler(path).put(key, value)
        val lines = Files.readAllLines(path)
        assertThat(lines).contains("export $key=$value").hasSize(1)
    }

    @Test
    fun `put should add property to existing file`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val key = "newline"
        val value = "some value"
        BashFileHandler(path).put(key, value)
        val lines = Files.readAllLines(path)
        val line = "export $key=$value"
        assertThat(lines).contains(line)
        assertThat(lines.size).isGreaterThan(1)
        assertThat(lines.last()).isEqualTo(line)
    }

    @Test
    fun `put should not add property if key already exists`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val initSize = Files.readAllLines(path).size
        val key = "JAVA_HOME"
        val value = "/some/path"
        BashFileHandler(path).put(key, value)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it.startsWith("export $key") }).hasSize(1)
    }

    @Test
    fun `put should not add property if key already exists ignoring case`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val initSize = Files.readAllLines(path).size
        val key = "java_home"
        val value = "/some/path"
        BashFileHandler(path).put(key, value)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it.startsWith("export $key") }).hasSize(1)
    }

    @Test
    fun `put should edit property if key already exists`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val key = "JAVA_HOME"
        val value = "/some/path"
        BashFileHandler(path).put(key, value)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).contains("export $key=$value")
    }

    @Test
    fun `put should edit property if key already exists ignoring case`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val key = "java_home"
        val value = "/some/path"
        BashFileHandler(path).put(key, value)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).contains("export $key=$value")
    }

    @Test
    fun `put should edit value if key exists`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val key = "MONGO_HOME"
        val updatedValue = "/mongo/path"
        BashFileHandler(path).put(key, updatedValue)
        val updatedLines = Files.readAllLines(path)
        val exportKey = "export $key"
        assertThat(updatedLines.first { it.startsWith(exportKey) }).isEqualTo("$exportKey=$updatedValue")
    }

    @Test
    fun `remove should remove property from an existing file`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val lines = Files.readAllLines(path)
        val key = "JAVA_HOME"
        val exportKey = "export $key"
        assertThat(lines.filter { it.startsWith(exportKey) }).isNotEmpty
        BashFileHandler(path).remove(key)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(lines.size - 1)
        assertThat(updatedLines.filter { it.startsWith(exportKey) }).isEmpty()
    }

    @Test
    fun `remove should remove property ignoring case`() {
        val path = tmpDirPath().resolve(".not_empty_bash_profile")
        val lines = Files.readAllLines(path)
        val key = "java_home"
        val exportKey = "export ${key.toUpperCase()}"
        assertThat(lines.filter { it.startsWith(exportKey) }).isNotEmpty
        BashFileHandler(path).remove(key)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(lines.size - 1)
        assertThat(updatedLines.filter { it.startsWith(exportKey, true) }).isEmpty()
    }

    private fun createEmptyBashFile() = Files.createFile(tmpDirPath().resolve(".empty_bash_profile"))

    private fun createNotEmptyBashFile() {
        val path = Files.createFile(tmpDirPath().resolve(".not_empty_bash_profile"))
        Files.write(path, listOf("alias ll='ls -l'", "export JAVA_HOME=/Library/Java"))
    }
}