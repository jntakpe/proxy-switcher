package com.github.jntakpe.proxyswitcher

data class ProxyAddress(val host: String, val port: String, val nonProxies: List<String> = emptyList())