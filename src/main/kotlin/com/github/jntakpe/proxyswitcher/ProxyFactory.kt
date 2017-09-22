package com.github.jntakpe.proxyswitcher


fun createProxies(keys: List<String>, platform: Platform, proxyAddress: ProxyAddress): List<Proxy> {
    return keys
            .map { AppKey.valueOf(it.toUpperCase()) }
            .map { keyToProxy(it, platform, proxyAddress) }
}

private fun keyToProxy(key: AppKey, platform: Platform, proxyAddress: ProxyAddress) = when (key) {
    AppKey.BASH -> BashProxy(platform, proxyAddress)
    AppKey.GRADLE -> GradleProxy(platform, proxyAddress)
}