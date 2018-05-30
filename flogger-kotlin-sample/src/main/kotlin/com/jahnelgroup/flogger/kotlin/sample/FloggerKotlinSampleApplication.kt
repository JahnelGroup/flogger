package com.jahnelgroup.flogger.kotlin.sample

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FloggerKotlinSampleApplication

val LOG : Logger = LoggerFactory.getLogger(FloggerKotlinSampleApplication::class.java)

fun main(args: Array<String>) {
    runApplication<FloggerKotlinSampleApplication>(*args)
    LOG.info("is logging working?")
}
