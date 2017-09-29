package com.github.jntakpe.proxyswitcher.proxy

import com.github.jntakpe.proxyswitcher.constant.Protocol.HTTP
import com.github.jntakpe.proxyswitcher.filehandler.FileHandler
import com.github.jntakpe.proxyswitcher.filehandler.PropertyFileHandler
import com.github.jntakpe.proxyswitcher.model.ProxyAddress
import com.github.jntakpe.proxyswitcher.platform.Platform
import java.io.FileNotFoundException
import java.nio.file.Files

class NpmProxy(platform: Platform, private val proxyAddress: ProxyAddress) : Proxy {

    private val fileHandler: FileHandler

    companion object {
        const val HTTP_PROXY = "proxy"
        const val HTTPS_PROXY = "https-proxy"
    }

    init {
        val userHome = platform.userHome()
        if (!Files.exists(userHome)) {
            throw FileNotFoundException("Unable to find user home directory : ${userHome.toAbsolutePath()}")
        }
        fileHandler = PropertyFileHandler(userHome.resolve(".npmrc"))
    }

    override fun enable() {
        fileHandler.put(HTTP_PROXY, "${HTTP.name.toLowerCase()}://${proxyAddress.host}:${proxyAddress.port}")
        fileHandler.put(HTTPS_PROXY, "${HTTP.name.toLowerCase()}://${proxyAddress.host}:${proxyAddress.port}")
    }

    override fun disable() {
        fileHandler.remove(HTTP_PROXY)
        fileHandler.remove(HTTPS_PROXY)
    }
}