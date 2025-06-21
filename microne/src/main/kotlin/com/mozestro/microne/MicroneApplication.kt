package com.mozestro.microne

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroneApplication

fun main(args: Array<String>) {
    runApplication<MicroneApplication>(*args)
}
