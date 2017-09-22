package com.github.jntakpe.proxyswitcher

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

}