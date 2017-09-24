package com.github.jntakpe.proxyswitcher.utils

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class ArgumentsUtilsTest {

    @Test
    fun `init should fail if mandatory arguments doesn't exist`() {
        assertThat(catchThrowable { ArgumentsUtils(arrayOf("enable")) }).isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `enable should be enabled`() {
        assertThat(ArgumentsUtils(arrayOf("enable", "", "", "")).enable()).isTrue()
        assertThat(ArgumentsUtils(arrayOf("ENABLE", "", "", "")).enable()).isTrue()
        assertThat(ArgumentsUtils(arrayOf("enabled", "", "", "")).enable()).isTrue()
    }

    @Test
    fun `enable should be disabled`() {
        assertThat(ArgumentsUtils(arrayOf("disable", "", "", "")).enable()).isFalse()
        assertThat(ArgumentsUtils(arrayOf("DISABLE", "", "", "")).enable()).isFalse()
        assertThat(ArgumentsUtils(arrayOf("disabled", "", "", "")).enable()).isFalse()
    }

    @Test
    fun `keys should transformed to singleton list`() {
        assertThat(ArgumentsUtils(arrayOf("enable", "bash", "", "")).keys()).hasSize(1).containsOnly("bash")
    }

    @Test
    fun `keys should transformed to list`() {
        assertThat(ArgumentsUtils(arrayOf("enable", "bash,gradle", "", "")).keys()).hasSize(2).containsOnly("bash", "gradle")
    }

    @Test
    fun `keys should transformed to list with values trimed`() {
        assertThat(ArgumentsUtils(arrayOf("enable", "bash , gradle", "", "")).keys()).hasSize(2).containsOnly("bash", "gradle")
    }

    @Test
    fun `proxyAddress should contain host`() {
        val proxy = "some.proxy.host.value"
        assertThat(ArgumentsUtils(arrayOf("enable", "bash , gradle", proxy, "port")).proxyAddress().host).isEqualTo(proxy)
    }

    @Test
    fun `proxyAddress should contain port`() {
        val port = "8080"
        assertThat(ArgumentsUtils(arrayOf("enable", "bash , gradle", "proxy", port)).proxyAddress().port).isEqualTo(port)
    }

    @Test
    fun `proxyAddress should contain single non proxy`() {
        val nonProxy = "localhost"
        val arg = ArgumentsUtils(arrayOf("enable", "bash , gradle", "proxy", "port", nonProxy)).proxyAddress().nonProxies
        assertThat(arg).hasSize(1).containsOnly(nonProxy)
    }

    @Test
    fun `proxyAddress should contain couple non proxy`() {
        val nonProxy = "localhost,127.0.0.1"
        val arg = ArgumentsUtils(arrayOf("enable", "bash , gradle", "proxy", "port", nonProxy)).proxyAddress().nonProxies
        assertThat(arg).hasSize(2).containsOnly("localhost", "127.0.0.1")
    }

    @Test
    fun `proxyAddress should contain couple non proxy trimed`() {
        val nonProxy = "localhost , 127.0.0.1"
        val arg = ArgumentsUtils(arrayOf("enable", "bash , gradle", "proxy", "port", nonProxy)).proxyAddress().nonProxies
        assertThat(arg).hasSize(2).containsOnly("localhost", "127.0.0.1")
    }

}