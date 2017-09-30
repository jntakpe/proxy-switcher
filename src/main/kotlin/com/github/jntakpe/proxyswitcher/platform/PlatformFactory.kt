package com.github.jntakpe.proxyswitcher.platform

fun createPlatform() = when {
    platformName().startsWith("mac", true) -> OSXPlatform()
    platformName().startsWith("windows", true) -> WindowsPlatform()
    else -> throw UnsupportedOperationException("Unsupported platform ${platformName()}")
}

private fun platformName() = System.getProperty("os.name")