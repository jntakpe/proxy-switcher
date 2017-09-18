package com.github.jntakpe.proxyswitcher

import com.github.jntakpe.proxyswitcher.GradleProperties.*
import com.github.jntakpe.proxyswitcher.Protocol.HTTP
import com.github.jntakpe.proxyswitcher.Protocol.HTTPS
import java.io.FileNotFoundException
import java.nio.file.Files

class GradleProxy(platform: Platform, private val address: ProxyAddress) : Proxy {

    private val fileHandler: PropertyFileHandler

    init {
        val gradleDir = platform.userHome().resolve(".gradle")
        if (!Files.exists(gradleDir)) {
            throw FileNotFoundException("Unable to find gradle configuration directory : ${gradleDir.toAbsolutePath()}")
        }
        val filePath = gradleDir.resolve("gradle.properties")
        fileHandler = PropertyFileHandler(filePath)
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
        fileHandler.put(prefixKey(HOST.key, protocol), address.host)
        fileHandler.put(prefixKey(PORT.key, protocol), address.port)
        if (address.nonProxies.isNotEmpty()) {
            fileHandler.put(prefixKey(NON_PROXY.key, protocol), address.nonProxies.joinToString("|"))
        }
    }

    private fun prefixKey(key: String, protocol: Protocol) = "systemProp.${protocol.value}.$key"

}