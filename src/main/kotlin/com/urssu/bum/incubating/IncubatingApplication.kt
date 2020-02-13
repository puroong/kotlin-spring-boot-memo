package com.urssu.bum.incubating

import com.urssu.bum.incubating.security.SecurityProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(SecurityProperty::class)
class IncubatingApplication

fun main(args: Array<String>) {
	runApplication<IncubatingApplication>(*args)
}
