package com.urssu.bum.incubating

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IncubatingApplication

fun main(args: Array<String>) {
	runApplication<IncubatingApplication>(*args)
}
