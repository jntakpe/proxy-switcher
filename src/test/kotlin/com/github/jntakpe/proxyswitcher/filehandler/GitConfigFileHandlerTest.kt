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

internal class GitConfigFileHandlerTest {

    @BeforeEach
    fun beforeEach() {
        cleanUpTmpDir()
        createEmptyGitFile()
        createNotEmptyGitFile()
    }

    @AfterEach
    fun afterEach() {
        cleanUpTmpDir()
    }

    @Test
    fun `init should create new file`() {
        val path = tmpDirPath().resolve(".gitconfig")
        GitConfigFileHandler(path)
        assertThat(path).exists()
    }

    @Test
    fun `init should fail if file does not exist`() {
        val path = tmpDirPath().resolve(".unknown_gitconfig")
        assertThat(catchThrowable { GitConfigFileHandler(path, false) }).isInstanceOf(FileNotFoundException::class.java)
    }

    @Test
    fun `put should add entry to empty file`() {
        val path = tmpDirPath().resolve(".empty_gitconfig")
        val key = "protocol"
        val value = "some.proxy.host:9080"
        GitConfigFileHandler(path).put(key, value)
        val lines = Files.lines(path)
        assertThat(lines).contains("[$key]", "    proxy = $key://$value").hasSize(2)
    }

    @Test
    fun `put should add property to existing file`() {
        val path = tmpDirPath().resolve(".not_empty_gitconfig")
        val key = "protocol"
        val value = "some.proxy.host:9080"
        GitConfigFileHandler(path).put(key, value)
        val lines = Files.readAllLines(path)
        val protocolLine = "[protocol]"
        val proxyLine = "    proxy = $key://$value"
        assertThat(lines).contains(protocolLine, proxyLine)
        assertThat(lines.size).isGreaterThan(2)
        assertThat(lines.takeLast(2)).containsOnly(protocolLine, proxyLine)
    }

    @Test
    fun `put should not add property if key already exists`() {
        val path = tmpDirPath().resolve(".not_empty_gitconfig")
        val initSize = Files.readAllLines(path).size
        val key = "http"
        val value = "some.proxy.host:9080"
        GitConfigFileHandler(path).put(key, value)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it == "[$key]" }).hasSize(1)
    }

    @Test
    fun `put should not add property if key already exists ignoring case`() {
        val path = tmpDirPath().resolve(".not_empty_gitconfig")
        val initSize = Files.readAllLines(path).size
        val key = "HTTP"
        val value = "some.proxy.host:9080"
        GitConfigFileHandler(path).put(key, value)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it == "[${key.toLowerCase()}]" }).hasSize(1)
    }

    @Test
    fun `put should edit value if key exists`() {
        val path = tmpDirPath().resolve(".not_empty_gitconfig")
        val initSize = Files.readAllLines(path).size
        val key = "http"
        val updatedValue = "some.updated.proxy.host:7080"
        GitConfigFileHandler(path).put(key, updatedValue)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it == "[$key]" }).hasSize(1)
        val valueIdx = updatedLines.indexOfFirst { it == "[$key]" } + 1
        assertThat(updatedLines[valueIdx]).isEqualTo("    proxy = $key://$updatedValue")
    }

    @Test
    fun `put should edit value if key exists ignore case`() {
        val path = tmpDirPath().resolve(".not_empty_gitconfig")
        val initSize = Files.readAllLines(path).size
        val key = "HTTP"
        val updatedValue = "some.updated.proxy.host:7080"
        GitConfigFileHandler(path).put(key, updatedValue)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(initSize)
        assertThat(updatedLines.filter { it.equals("[$key]", true) }).hasSize(1)
        val valueIdx = updatedLines.indexOfFirst { it.equals("[$key]", true) } + 1
        assertThat(updatedLines[valueIdx]).isEqualTo("    proxy = $key://$updatedValue")
    }

    @Test
    fun `remove should remove property from an existing file`() {
        val path = tmpDirPath().resolve(".not_empty_gitconfig")
        val lines = Files.readAllLines(path)
        val key = "http"
        assertThat(lines.filter { it == "[$key]" }).isNotEmpty
        assertThat(lines.map { it.trim() }.filter { it.startsWith("proxy") }).isNotEmpty.hasSize(2)
        GitConfigFileHandler(path).remove(key)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(lines.size - 2)
        assertThat(updatedLines.filter { it == "[$key]" }).isEmpty()
        assertThat(updatedLines.map { it.trim() }.filter { it.startsWith("proxy") }).isNotEmpty.hasSize(1)
    }

    @Test
    fun `remove should remove property ignoring case`() {
        val path = tmpDirPath().resolve(".not_empty_gitconfig")
        val lines = Files.readAllLines(path)
        val key = "HTTP"
        assertThat(lines.filter { it.equals("[$key]", true) }).isNotEmpty
        assertThat(lines.map { it.trim() }.filter { it.startsWith("proxy") }).isNotEmpty.hasSize(2)
        GitConfigFileHandler(path).remove(key)
        val updatedLines = Files.readAllLines(path)
        assertThat(updatedLines).hasSize(lines.size - 2)
        assertThat(updatedLines.filter { it == "[$key]" }).isEmpty()
        assertThat(updatedLines.map { it.trim() }.filter { it.startsWith("proxy") }).isNotEmpty.hasSize(1)
    }

    private fun createEmptyGitFile() = Files.createFile(tmpDirPath().resolve(".empty_gitconfig"))

    private fun createNotEmptyGitFile() {
        val path = Files.createFile(tmpDirPath().resolve(".not_empty_gitconfig"))
        Files.write(path, listOf("[http]", "    proxy = http://some.proxy.host:8080", "[https]", "    proxy = http://some.proxy.host:8080"))
    }
}