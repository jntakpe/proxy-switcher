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

class PropertyFileHandlerTest {

    @BeforeEach
    fun beforeEach() {
        cleanUpTmpDir()
        createEmptyPropertyFile()
        createNotEmptyPropertyFile()
    }

    @AfterEach
    fun afterEach() {
        cleanUpTmpDir()
    }

    @Test
    fun `init should create new file`() {
        val path = tmpDirPath().resolve("new_file.properties")
        PropertyFileHandler(path)
        assertThat(path).exists()
    }

    @Test
    fun `init should fail if file does not exist`() {
        val path = tmpDirPath().resolve("unknown_file.properties")
        assertThat(catchThrowable { PropertyFileHandler(path, false) }).isInstanceOf(FileNotFoundException::class.java)
    }

    @Test
    fun `put should add property to empty file`() {
        val path = tmpDirPath().resolve("empty.properties")
        val key = "newline"
        val value = "some value"
        PropertyFileHandler(path).put(key, value)
        val lines = Files.readAllLines(path)
        assertThat(lines).contains("$key=$value").hasSize(1)
    }

    @Test
    fun `put should add property to existing file`() {
        val path = tmpDirPath().resolve("not_empty.properties")
        val key = "newline"
        val value = "some value"
        PropertyFileHandler(path).put(key, value)
        val lines = Files.readAllLines(path)
        val line = "$key=$value"
        assertThat(lines).contains(line)
        assertThat(lines.size).isGreaterThan(1)
        assertThat(lines.last()).isEqualTo(line)
    }

    @Test
    fun `put should not add property if key already exists`() {
        val path = tmpDirPath().resolve("not_empty.properties")
        val initSize = Files.readAllLines(path).size
        val key = "firstKey"
        PropertyFileHandler(path).put(key, "someVal")
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it.startsWith(key) }).hasSize(1)
    }

    @Test
    fun `put should not add property if key already exists ignoring case`() {
        val path = tmpDirPath().resolve("not_empty.properties")
        val initSize = Files.readAllLines(path).size
        val key = "FIRSTKEY"
        PropertyFileHandler(path).put(key, "someVal")
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it.startsWith(key, true) }).hasSize(1)
    }

    @Test
    fun `put should edit value if key exists`() {
        val path = tmpDirPath().resolve("not_empty.properties")
        val key = "firstKey"
        val updatedValue = "updatedValue"
        PropertyFileHandler(path).put(key, updatedValue)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines.first { it.startsWith(key) }).isEqualTo("$key=$updatedValue")
    }

    @Test
    fun `put should edit value if key exists ignoring case`() {
        val path = tmpDirPath().resolve("not_empty.properties")
        val key = "FIRSTKEY"
        val updatedValue = "updatedValue"
        PropertyFileHandler(path).put(key, updatedValue)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines.first { it.startsWith(key) }).isEqualTo("$key=$updatedValue")
    }

    @Test
    fun `remove should remove property from an existing file`() {
        val path = tmpDirPath().resolve("not_empty.properties")
        val lines = Files.readAllLines(path)
        val key = "secondKey"
        assertThat(lines.filter { it.startsWith(key) }).isNotEmpty
        PropertyFileHandler(path).remove(key)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(lines.size - 1)
        assertThat(updatedLines.filter { it.startsWith(key) }).isEmpty()
    }

    @Test
    fun `remove should remove property ignoring case`() {
        val path = tmpDirPath().resolve("not_empty.properties")
        val lines = Files.readAllLines(path)
        val key = "SECONDKEY"
        assertThat(lines.filter { it.startsWith("secondKey") }).isNotEmpty
        PropertyFileHandler(path).remove(key)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(lines.size - 1)
        assertThat(updatedLines.filter { it.startsWith(key, true) }).isEmpty()
    }

    private fun createEmptyPropertyFile() = Files.createFile(tmpDirPath().resolve("empty.properties"))

    private fun createNotEmptyPropertyFile() {
        val path = Files.createFile(tmpDirPath().resolve("not_empty.properties"))
        Files.write(path, listOf("firstKey=firstVal", "secondKey=secondVal"))
    }

}