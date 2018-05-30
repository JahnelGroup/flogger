package com.jahnelgroup.flogger.kotlin.sample

import com.jahnelgroup.flogger.BindParam
import com.jahnelgroup.flogger.BindReturn
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TestServiceBean: TestService {

    val LOG = LoggerFactory.getLogger(TestController::class.java)

    @BindReturn(expand = true)
    override fun test2(@BindParam("testObjParamInTest2") testObj: TestObject): TestObject {
        LOG.info("IN TEST2()")
        testObj.name = "newName";
        return testObj;
    }

    @BindReturn("stringReturnTest1")
    override fun test(@BindParam("stringParamTest1") string: String): String {
        LOG.info("IN TEST()");
        return "testReturn";
    }
}
