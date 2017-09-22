package com.github.jntakpe.proxyswitcher

fun main(args: Array<String>) {
    val arg = ArgumentsExtractor(args)
    createProxies(arg.keys(), createPlatform(), arg.proxyAddress()).forEach { if (arg.enable()) it.enable() else it.disable() }
}