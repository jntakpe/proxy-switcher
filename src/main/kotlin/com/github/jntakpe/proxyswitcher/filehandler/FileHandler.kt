package com.github.jntakpe.proxyswitcher.filehandler

interface FileHandler {

    fun put(key: String, value: String)

    fun remove(key: String)

}