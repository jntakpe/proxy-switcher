package com.github.jntakpe.proxyswitcher

import com.github.jntakpe.proxyswitcher.AppMode.DISABLE
import com.github.jntakpe.proxyswitcher.AppMode.ENABLE

class ArgumentsExtractor(private val args: Array<String>) {

    init {
        if (args.size < 4) throw IllegalStateException("Program arguments should at least contain 4 arguments")
    }

    fun enable(): Boolean {
        val (enable) = args
        return when {
            enable.startsWith(ENABLE.name, true) -> true
            enable.startsWith(DISABLE.name, true) -> false
            else -> throw IllegalStateException("Unable to enable or disable proxies. Value $enable should be 'ENABLE' or 'DISABLE'")
        }
    }

    fun keys(): List<String> {
        val (_, keys) = args
        return keys.split(",").map { it.trim() }
    }

    fun proxyAddress(): ProxyAddress {
        val (_, _, host, port) = args
        return if (args.size < 5) ProxyAddress(host, port) else ProxyAddress(host, port, args[4].split(",").map { it.trim() })
    }

}