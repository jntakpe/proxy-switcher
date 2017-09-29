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

internal class GitProxyTest {

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
        GitProxy(TestPlatform(), ProxyAddress("", "")).enable()
        assertThat(TestPlatform().userHome().resolve(".gitconfig")).exists()
    }

    @Test
    fun `enable should add http proxy property`() {
        val host = "some.proxy.host.value"
        val port = "8080"
        GitProxy(TestPlatform(), ProxyAddress(host, port)).enable()
        assertThat(configLines()).contains("[http]")
        assertThat(configLines().map { it.trim() }).contains("proxy = http://$host:$port")
    }

    @Test
    fun `enable should add https proxy property`() {
        val host = "some.proxy.host.value"
        val port = "8080"
        GitProxy(TestPlatform(), ProxyAddress(host, port)).enable()
        assertThat(configLines()).contains("[https]")
        assertThat(configLines().map { it.trim() }).contains("proxy = http://$host:$port")
    }

    @Test
    fun `disable should remove all properties related to proxy configuration`() {
        Files.copy(samplePath(), configFile())
        assertThat(configLines()).isNotEmpty
        GitProxy(TestPlatform(), ProxyAddress("", "")).disable()
        val proxyLines = configLines().map { it.trim() }.filter { it.startsWith("[http]") || it.startsWith("[https]") || it.startsWith("proxy") }
        assertThat(proxyLines).isEmpty()
    }

    private fun configFile() = TestPlatform().userHome().resolve(".gitconfig")

    private fun configLines() = Files.readAllLines(configFile())

    private fun samplePath() = Paths.get("src", "test", "resources", "sample", ".gitconfig")

}