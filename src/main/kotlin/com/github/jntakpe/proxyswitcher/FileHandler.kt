package com.github.jntakpe.proxyswitcher

interface FileHandler {

    fun put(key: String, value: String)

    fun remove(key: String)

}