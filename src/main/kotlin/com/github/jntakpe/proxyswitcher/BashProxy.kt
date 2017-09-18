package com.github.jntakpe.proxyswitcher

import java.io.FileNotFoundException
import java.nio.file.Files

class BashProxy(platform: Platform, private val proxyAddress: ProxyAddress) : Proxy {

    private val fileHandler: FileHandler

    init {
        val userHome = platform.userHome()
        if (!Files.exists(userHome)) {
            throw FileNotFoundException("Unable to find user home directory : ${userHome.toAbsolutePath()}")
        }
        fileHandler = BashFileHandler(userHome.resolve(".bash_profile"))
    }

    override fun enable() {

    }

    override fun disable() {
        TODO("not implemented")
    }

}