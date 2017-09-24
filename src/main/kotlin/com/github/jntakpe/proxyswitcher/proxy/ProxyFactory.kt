package com.github.jntakpe.proxyswitcher.proxy

import com.github.jntakpe.proxyswitcher.constant.AppKey
import com.github.jntakpe.proxyswitcher.model.ProxyAddress
import com.github.jntakpe.proxyswitcher.platform.Platform


fun createProxies(keys: List<String>, platform: Platform, proxyAddress: ProxyAddress): List<Proxy> {
    return keys
            .map { AppKey.valueOf(it.toUpperCase()) }
            .map { keyToProxy(it, platform, proxyAddress) }
}

private fun keyToProxy(key: AppKey, platform: Platform, proxyAddress: ProxyAddress) = when (key) {
    AppKey.BASH -> BashProxy(platform, proxyAddress)
    AppKey.GRADLE -> GradleProxy(platform, proxyAddress)
}