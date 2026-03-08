package com.lomeone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LomeoneApiApplication

fun main(args: Array<String>) {
    runApplication<LomeoneApiApplication>(*args)
}
