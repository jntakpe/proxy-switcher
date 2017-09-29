package com.github.jntakpe.proxyswitcher.proxy

import com.github.jntakpe.proxyswitcher.model.ProxyAddress
import com.github.jntakpe.proxyswitcher.platform.TestPlatform
import com.github.jntakpe.proxyswitcher.utils.cleanUpTmpDir
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class NpmProxyTest {

    @BeforeEach
    fun beforeEach() {
        cleanUpTmpDir()
    }

    @AfterEach
    fun afterEach() {
        cleanUpTmpDir()
    }

    @Test
    fun `enable should create npm config file if missing`() {
        NpmProxy(TestPlatform(), ProxyAddress("", "")).enable()
        assertThat(TestPlatform().userHome().resolve(".npmrc")).exists()
    }

    @Test
    fun `enable should add http proxy property`() {
        val host = "some.proxy.host.value"
        val port = "8080"
        NpmProxy(TestPlatform(), ProxyAddress(host, port)).enable()
        assertThat(configLines()).contains("proxy=http://$host:$port")
    }

    @Test
    fun `enable should add https proxy property`() {
        val host = "some.proxy.host.value"
        val port = "8080"
        NpmProxy(TestPlatform(), ProxyAddress(host, port)).enable()
        assertThat(configLines()).contains("https-proxy=http://$host:$port")
    }

    @Test
    fun `disable should remove all properties related to proxy configuration`() {
        Files.copy(samplePath(), configFile())
        assertThat(configLines()).isNotEmpty
        NpmProxy(TestPlatform(), ProxyAddress("", "")).disable()
        val proxyLines = configLines().filter { it.startsWith("proxy") || it.startsWith("https-proxy") }
        assertThat(proxyLines).isEmpty()
    }

    private fun configFile() = TestPlatform().userHome().resolve(".npmrc")

    private fun configLines() = Files.readAllLines(configFile())

    private fun samplePath() = Paths.get("src", "test", "resources", "sample", ".npmrc")
}