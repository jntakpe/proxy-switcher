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
    fun `enable should create bash profile file if missing`() {
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
    fun `enable should not add no proxy property`() {
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

    @Test
    fun `disable should remove all properties related to proxy configuration`() {
        Files.copy(samplePath(), configFile())
        assertThat(configLines()).isNotEmpty
        BashProxy(TestPlatform(), ProxyAddress("", "")).disable()
        val proxyLines = configLines()
                .filter { it.startsWith("export HTTP_PROXY") || it.startsWith("export HTTPS_PROXY") || it.startsWith("export NO_PROXY") }
        assertThat(proxyLines).isEmpty()
    }

    private fun configFile() = TestPlatform().userHome().resolve(".bash_profile")

    private fun configLines() = Files.readAllLines(configFile())

    private fun samplePath() = Paths.get("src", "test", "resources", "sample", ".bash_profile")

}

