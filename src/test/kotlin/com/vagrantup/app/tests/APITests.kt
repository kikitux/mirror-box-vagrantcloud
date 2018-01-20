package com.vagrantup.app.tests

import com.vagrantup.app.getApiUserData
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class APITests {
    @Test fun testGetAPIUserData(){
        val userHashicorp = getApiUserData("hashicorp")
        assertNotNull(userHashicorp)
        assertEquals("hashicorp",userHashicorp.username)
    }


}