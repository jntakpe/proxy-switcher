package com.github.jntakpe.proxyswitcher

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

internal class GradleProxyTest {

    @BeforeEach
    fun beforeEach() {
        cleanUpTmpDir()
        createGradleConfigurationDirectory()
    }

    @AfterEach
    fun afterEach() {
        cleanUpTmpDir()
    }

    @Test
    fun `init should throw if gradle configuration directory is missing`() {
        cleanUpTmpDir()
        val catchThrowable = catchThrowable { GradleProxy(TestPlatform(), ProxyAddress("", "")) }
        assertThat(catchThrowable).isInstanceOf(FileNotFoundException::class.java)
    }

    @Test
    fun `enable should create gradle properties file if missing`() {
        GradleProxy(TestPlatform(), ProxyAddress("", "")).enable()
        assertThat(TestPlatform().userHome().resolve(".gradle").resolve("gradle.properties")).exists()
    }

    @Test
    fun `enable should add http proxy host property`() {
        val proxyHost = "some.proxy.host.value"
        GradleProxy(TestPlatform(), ProxyAddress(proxyHost, "")).enable()
        assertThat(configLines()).contains("systemProp.http.proxyHost=$proxyHost")
    }

    @Test
    fun `enable should add http proxy port property`() {
        val proxyPort = "8080"
        GradleProxy(TestPlatform(), ProxyAddress("", proxyPort)).enable()
        assertThat(configLines()).contains("systemProp.http.proxyPort=$proxyPort")
    }

    @Test
    fun `enable should not add non proxy key if list empty`() {
        GradleProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080")).enable()
        assertThat(configLines().any { it.startsWith("systemProp.http.nonProxyHosts") }).isFalse()
    }

    @Test
    fun `enable should add non proxy key`() {
        val nonProxyValue = listOf("localhost")
        GradleProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080", nonProxyValue)).enable()
        val nonProxyKey = "systemProp.http.nonProxyHosts"
        assertThat(configLines().any { it.startsWith(nonProxyKey) }).isTrue()
        assertThat(configLines()).contains("$nonProxyKey=localhost")
    }

    @Test
    fun `enable should add non proxy values concatenated with pipes`() {
        val nonProxyValue = listOf("localhost", "127.0.0.1", "10.10.1.*")
        GradleProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080", nonProxyValue)).enable()
        val nonProxyKey = "systemProp.http.nonProxyHosts"
        assertThat(configLines()).contains("$nonProxyKey=localhost|127.0.0.1|10.10.1.*")
    }

    @Test
    fun `enable should https properties`() {
        GradleProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080", listOf("localhost"))).enable()
        val httpsLines = configLines().filter { it.startsWith("systemProp.https.") }
        val httpLines = configLines().filter { it.startsWith("systemProp.http.") }
        assertThat(httpsLines).hasSameSizeAs(httpLines).hasSize(3)
    }

    @Test
    fun `enable should create complete gradle properties file equals to sample file`() {
        val nonProxyValue = listOf("localhost", "127.0.0.1", "10.10.1.*")
        GradleProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080", nonProxyValue)).enable()
        assertThat(configLines()).isEqualTo(Files.readAllLines(samplePath()))
    }

    @Test
    fun `disable should remove all properties related to proxy configuration`() {
        Files.copy(samplePath(), configFile())
        assertThat(configLines()).isNotEmpty
        GradleProxy(TestPlatform(), ProxyAddress("", "")).disable()
        assertThat(configLines()).isEmpty()
    }

    private fun samplePath() = Paths.get("src", "test", "resources", "sample", "gradle.properties")

    private fun createGradleConfigurationDirectory() = Files.createDirectory(TestPlatform().userHome().resolve(".gradle"))

    private fun configFile() = TestPlatform().userHome().resolve(".gradle").resolve("gradle.properties")

    private fun configLines() = Files.readAllLines(configFile())
}