package com.github.jntakpe.proxyswitcher

import com.github.jntakpe.proxyswitcher.platform.createPlatform
import com.github.jntakpe.proxyswitcher.proxy.createProxies
import com.github.jntakpe.proxyswitcher.utils.ArgumentsUtils

fun main(args: Array<String>) {
    val arg = ArgumentsUtils(args)
    createProxies(arg.keys(), createPlatform(), arg.proxyAddress()).forEach { if (arg.enable()) it.enable() else it.disable() }
}