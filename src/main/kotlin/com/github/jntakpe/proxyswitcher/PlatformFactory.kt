package com.github.jntakpe.proxyswitcher

fun createPlatform() = when {
    platformName().startsWith("mac", true) -> OSXPlatform()
    else -> throw UnsupportedOperationException("Unsupported platform ${platformName()}")
}

private fun platformName() = System.getProperty("os.name")