package com.github.jntakpe.proxyswitcher

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal class PropertyFileHandlerTest {

    @BeforeEach
    fun beforeEach() {
        val propertiesDir = propertyDir()
        cleanUpDirectory(propertiesDir)
        createEmptyPropertyFile(propertiesDir)
        createNotEmptyPropertyFile(propertiesDir)
    }

    @Test
    fun `init should create new file`() {
        val path = propertyDir().resolve("new_file.properties")
        PropertyFileHandler(path)
        assertThat(path).exists()
    }

    @Test
    fun `init should fail if file does not exist`() {
        val path = propertyDir().resolve("unknown_file.properties")
        assertThat(catchThrowable { PropertyFileHandler(path, false) }).isInstanceOf(FileNotFoundException::class.java)
    }

    @Test
    fun `put should add property to empty file`() {
        val path = propertyDir().resolve("empty.properties")
        val key = "newline"
        val value = "some value"
        PropertyFileHandler(path).put(key, value)
        val lines = Files.lines(path)
        assertThat(lines).contains("$key=$value").hasSize(1)
    }

    @Test
    fun `put should add property to existing file`() {
        val path = propertyDir().resolve("not_empty.properties")
        val key = "newline"
        val value = "some value"
        PropertyFileHandler(path).put(key, value)
        val lines = Files.readAllLines(path)
        val line = "$key=$value"
        assertThat(lines).contains(line)
        assertThat(lines.size).isGreaterThan(1)
        assertThat(lines.last()).isEqualTo(line)
    }

    private fun propertyDir() = Paths.get("src", "test", "resources", "properties")

    private fun createEmptyPropertyFile(propertyDir: Path) {
        Files.createFile(propertyDir.resolve("empty.properties"))
    }

    private fun createNotEmptyPropertyFile(propertyDir: Path) {
        val path = Files.createFile(propertyDir.resolve("not_empty.properties"))
        Files.write(path, listOf("firstKey=firstVal", "secondKey=secondVal"))
    }

    private fun cleanUpDirectory(propertyDir: Path) {
        Files.list(propertyDir).forEach { Files.delete(it) }
    }

}