package com.github.jntakpe.proxyswitcher

import java.nio.file.Paths

interface Platform {

    fun userHome() = Paths.get(System.getProperty("user.home"))

}