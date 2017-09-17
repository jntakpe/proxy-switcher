package com.github.jntakpe.proxyswitcher

import com.github.jntakpe.proxyswitcher.Protocol.HTTP
import com.github.jntakpe.proxyswitcher.Protocol.HTTPS
import java.io.FileNotFoundException
import java.nio.file.Files

class GradleProxy(private val platform: Platform, private val address: ProxyAddress) : Proxy {

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

    private fun applyProxyProperties(protocol: Protocol) {
        fileHandler.put(prefixKey("proxyHost", protocol), address.host)
        fileHandler.put(prefixKey("proxyPort", protocol), address.port)
        if (address.nonProxies.isNotEmpty()) {
            fileHandler.put(prefixKey("nonProxyHosts", protocol), address.nonProxies.joinToString("|"))
        }
    }

    override fun disable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun prefixKey(key: String, protocol: Protocol) = "systemProp.${protocol.value}.$key"

}