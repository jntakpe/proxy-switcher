package com.github.jntakpe.proxyswitcher

import com.github.jntakpe.proxyswitcher.BashProxyKeys.*
import com.github.jntakpe.proxyswitcher.Protocol.HTTP
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
        fileHandler.put(HTTP_PROXY.name, "${HTTP.value}://${proxyAddress.host}:${proxyAddress.port}")
        fileHandler.put(HTTPS_PROXY.name, "\$HTTP_PROXY")
        if (proxyAddress.nonProxies.isNotEmpty()) {
            fileHandler.put(NO_PROXY.name, proxyAddress.nonProxies.joinToString(","))
        }
    }

    override fun disable() {
        fileHandler.remove(HTTP_PROXY.name)
        fileHandler.remove(HTTPS_PROXY.name)
        fileHandler.remove(NO_PROXY.name)
    }

}