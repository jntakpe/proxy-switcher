package com.github.jntakpe.proxyswitcher

import java.nio.file.Paths

class TestPlatform : Platform {

    override fun userHome() = Paths.get("src", "test", "resources", "tmp")

}