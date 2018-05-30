package com.jahnelgroup.flogger.kotlin.sample

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController (val testService:TestService) {

    val LOG : Logger = LoggerFactory.getLogger(TestController::class.java)

    @GetMapping("/test")
    fun test() {
        LOG.info("BEFORE TEST()")
        testService.test("test1")
        LOG.info("AFTER TEST()");
    }

    @GetMapping("/test2")
    fun test2() {
        LOG.info("BEFORE TEST2()")
        testService.test2(TestObject(1, "firstTestObject"))
        LOG.info("AFTER TEST2()")
    }
}
