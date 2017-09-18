package com.github.jntakpe.proxyswitcher

enum class GradleProperties(val key: String) {
    HOST("proxyHost"),
    PORT("proxyPort"),
    NON_PROXY("nonProxyHosts")
}