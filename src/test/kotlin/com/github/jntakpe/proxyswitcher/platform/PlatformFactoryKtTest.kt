package com.github.jntakpe.proxyswitcher.platform

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assumptions.assumingThat
import org.junit.jupiter.api.Test


internal class PlatformFactoryKtTest {

    @Test
    fun `createPlatform should create OSX`() {
        assumingThat(System.getProperty("os.name").startsWith("mac", true), {
            assertThat(createPlatform()).isInstanceOf(OSXPlatform::class.java)
        })
    }

    @Test
    fun `createPlatform should create Windows`() {
        assumingThat(System.getProperty("os.name").startsWith("windows", true), {
            assertThat(createPlatform()).isInstanceOf(WindowsPlatform::class.java)
        })
    }

}