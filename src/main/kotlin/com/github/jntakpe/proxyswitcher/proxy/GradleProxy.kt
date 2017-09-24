package com.github.jntakpe.proxyswitcher.proxy

import com.github.jntakpe.proxyswitcher.constant.GradleProperties
import com.github.jntakpe.proxyswitcher.constant.GradleProperties.*
import com.github.jntakpe.proxyswitcher.constant.Protocol
import com.github.jntakpe.proxyswitcher.constant.Protocol.HTTP
import com.github.jntakpe.proxyswitcher.constant.Protocol.HTTPS
import com.github.jntakpe.proxyswitcher.filehandler.FileHandler
import com.github.jntakpe.proxyswitcher.filehandler.PropertyFileHandler
import com.github.jntakpe.proxyswitcher.model.ProxyAddress
import com.github.jntakpe.proxyswitcher.platform.Platform
import java.io.FileNotFoundException
import java.nio.file.Files

class GradleProxy(platform: Platform, private val proxyAddress: ProxyAddress) : Proxy {

    private val fileHandler: FileHandler

    init {
        val gradleDir = platform.userHome().resolve(".gradle")
        if (!Files.exists(gradleDir)) {
            throw FileNotFoundException("Unable to find gradle configuration directory : ${gradleDir.toAbsolutePath()}")
        }
        fileHandler = PropertyFileHandler(gradleDir.resolve("gradle.properties"))
    }

    override fun enable() {
        applyProxyProperties(HTTP)
        applyProxyProperties(HTTPS)
    }

    override fun disable() {
        GradleProperties.values()
                .flatMap { t -> listOf(HTTP, HTTPS).map { Pair(t.key, it) } }
                .map { prefixKey(it.first, it.second) }
                .forEach { fileHandler.remove(it) }
    }

    private fun applyProxyProperties(protocol: Protocol) {
        fileHandler.put(prefixKey(HOST.key, protocol), proxyAddress.host)
        fileHandler.put(prefixKey(PORT.key, protocol), proxyAddress.port)
        if (proxyAddress.nonProxies.isNotEmpty()) {
            fileHandler.put(prefixKey(NON_PROXY.key, protocol), proxyAddress.nonProxies.joinToString("|"))
        }
    }

    private fun prefixKey(key: String, protocol: Protocol) = "systemProp.${protocol.value}.$key"

}