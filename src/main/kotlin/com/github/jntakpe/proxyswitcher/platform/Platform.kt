package com.github.jntakpe.proxyswitcher.platform

import java.nio.file.Paths

interface Platform {

    fun userHome() = Paths.get(System.getProperty("user.home"))

}