package com.github.jntakpe.proxyswitcher.proxy

import com.github.jntakpe.proxyswitcher.constant.Protocol.HTTP
import com.github.jntakpe.proxyswitcher.constant.Protocol.HTTPS
import com.github.jntakpe.proxyswitcher.filehandler.FileHandler
import com.github.jntakpe.proxyswitcher.filehandler.GitConfigFileHandler
import com.github.jntakpe.proxyswitcher.model.ProxyAddress
import com.github.jntakpe.proxyswitcher.platform.Platform
import java.io.FileNotFoundException
import java.nio.file.Files

class GitProxy(platform: Platform, private val proxyAddress: ProxyAddress) : Proxy {

    private val fileHandler: FileHandler

    init {
        val userHome = platform.userHome()
        if (!Files.exists(userHome)) {
            throw FileNotFoundException("Unable to find user home directory : ${userHome.toAbsolutePath()}")
        }
        fileHandler = GitConfigFileHandler(userHome.resolve(".gitconfig"))
    }

    override fun enable() {
        fileHandler.put(HTTP.value, "${proxyAddress.host}:${proxyAddress.port}")
        fileHandler.put(HTTPS.value, "${proxyAddress.host}:${proxyAddress.port}")
    }

    override fun disable() {
        fileHandler.remove(HTTP.value)
        fileHandler.remove(HTTPS.value)
    }
}