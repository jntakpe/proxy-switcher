package com.github.jntakpe.proxyswitcher

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class BashProxyTest {

    @BeforeEach
    fun beforeEach() {
        cleanUpTmpDir()
    }

    @AfterEach
    fun afterEach() {
        cleanUpTmpDir()
    }

    @Test
    fun `enable should create bah profile file if missing`() {
        BashProxy(TestPlatform(), ProxyAddress("", "")).enable()
        assertThat(TestPlatform().userHome().resolve(".bash_profile")).exists()
    }

    @Test
    fun `enable should add http proxy property`() {
        BashProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080")).enable()
        assertThat(configLines()).contains("export HTTP_PROXY=http://some.proxy.host.value:8080")
    }

    @Test
    fun `enable should add https proxy property`() {
        BashProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080")).enable()
        assertThat(configLines()).contains("export HTTPS_PROXY=\$HTTP_PROXY")
    }

    @Test
    fun `enable should not add no proxy propert`() {
        BashProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080")).enable()
        assertThat(configLines().any { it.startsWith("export NO_PROXY") }).isFalse()
    }

    @Test
    fun `enable should add non proxy key`() {
        val nonProxyValue = listOf("localhost")
        BashProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080", nonProxyValue)).enable()
        val nonProxyKey = "export NO_PROXY"
        assertThat(configLines().any { it.startsWith(nonProxyKey) }).isTrue()
        assertThat(configLines()).contains("$nonProxyKey=localhost")
    }

    @Test
    fun `enable should add non proxy values concatenated with pipes`() {
        val nonProxyValue = listOf("localhost", "127.0.0.1", "10.10.1.*")
        BashProxy(TestPlatform(), ProxyAddress("some.proxy.host.value", "8080", nonProxyValue)).enable()
        val nonProxyKey = "export NO_PROXY"
        assertThat(configLines()).contains("$nonProxyKey=localhost,127.0.0.1,10.10.1.*")
    }

    private fun configFile() = TestPlatform().userHome().resolve(".bash_profile")

    private fun configLines() = Files.readAllLines(configFile())

}

