package com.github.jntakpe.proxyswitcher

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.nio.file.Files

internal class FileUtilsKtTest {

    @Test
    fun `create if missing should not create file if create flag false`() {
        val path = resourcesDirPath().resolve("sample").resolve("gradle.properties")
        createIfMissing(path, false)
        assertThat(Files.isSameFile(path, resourcesDirPath().resolve("sample").resolve("gradle.properties")))
        assertThat(Files.readAllLines(path)).isNotEmpty
    }

    @Test
    fun `create if missing should not create file if file exists`() {
        val path = resourcesDirPath().resolve("sample").resolve("gradle.properties")
        createIfMissing(path, true)
        assertThat(Files.isSameFile(path, resourcesDirPath().resolve("sample").resolve("gradle.properties")))
        assertThat(Files.readAllLines(path)).isNotEmpty
    }

    @Test
    fun `create if missing should fail if file doesn't exists and not asked to create it`() {
        val path = tmpDirPath().resolve("do_not_create.txt")
        assertThat(catchThrowable { createIfMissing(path, false) }).isInstanceOf(FileNotFoundException::class.java)
        assertThat(path).doesNotExist()
    }

    @Test
    fun `create if missing should create a new file`() {
        val path = tmpDirPath().resolve("new_file.txt")
        createIfMissing(path, true)
        assertThat(path).exists()
        assertThat(Files.readAllLines(path)).isEmpty()
    }

    @Test
    fun `key exists should find key if delimiter is equals sign`() {
        val samplePath = resourcesDirPath().resolve("sample")
        val gradlePath = samplePath.resolve("gradle.properties")
        val bashPath = samplePath.resolve(".bash_profile")
        assertThat(keyExists(gradlePath, "systemProp.https.proxyHost", "=")).isTrue()
        assertThat(keyExists(bashPath, "export HTTP_PROXY", "=")).isTrue()
    }

    @Test
    fun `key exists should find key if ignoring case`() {
        val samplePath = resourcesDirPath().resolve("sample")
        val gradlePath = samplePath.resolve("gradle.properties")
        val bashPath = samplePath.resolve(".bash_profile")
        assertThat(keyExists(gradlePath, "systemProp.HTTPS.proxyHost", "=")).isTrue()
        assertThat(keyExists(bashPath, "EXPORT HTTP_PROXY", "=")).isTrue()
    }

    @Test
    fun `key exists should not find key`() {
        val samplePath = resourcesDirPath().resolve("sample")
        val gradlePath = samplePath.resolve("gradle.properties")
        val bashPath = samplePath.resolve(".bash_profile")
        assertThat(keyExists(gradlePath, "systemProp.ftp.proxyHost", "=")).isFalse()
        assertThat(keyExists(bashPath, "export FTP_PROXY", "=")).isFalse()
    }

}