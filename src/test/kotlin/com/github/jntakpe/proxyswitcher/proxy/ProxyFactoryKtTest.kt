package com.github.jntakpe.proxyswitcher.proxy

import com.github.jntakpe.proxyswitcher.model.ProxyAddress
import com.github.jntakpe.proxyswitcher.platform.OSXPlatform
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ProxyFactoryKtTest {

    @Test
    fun `createProxies should not create any proxy`() {
        assertThat(createProxies(emptyList(), OSXPlatform(), ProxyAddress("", ""))).isEmpty()
    }

    @Test
    fun `createProxies should create gradle proxy`() {
        val proxies = createProxies(listOf("GRADLE"), OSXPlatform(), ProxyAddress("", ""))
        assertThat(proxies).hasSize(1).allMatch { it is GradleProxy }
    }

    @Test
    fun `createProxies should create gradle proxy ignoring case`() {
        val proxies = createProxies(listOf("gradle"), OSXPlatform(), ProxyAddress("", ""))
        assertThat(proxies).hasSize(1).allMatch { it is GradleProxy }
    }

    @Test
    fun `createProxies should create bash proxy`() {
        val proxies = createProxies(listOf("BASH"), OSXPlatform(), ProxyAddress("", ""))
        assertThat(proxies).hasSize(1).allMatch { it is BashProxy }
    }

    @Test
    fun `createProxies should create two proxies`() {
        val proxies = createProxies(listOf("bash", "gradle"), OSXPlatform(), ProxyAddress("", ""))
        assertThat(proxies).hasSize(2)
    }

}