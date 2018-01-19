package com.hashicorp.checkpoint.tests


import com.hashicorp.checkpoint.getCheckPointData
import org.junit.Test
import kotlin.test.assertNotNull


class CheckPointTests {
    @Test fun testVagrant(){
        val vagrantVersion = getCheckPointData("vagrant")
        assertNotNull(vagrantVersion.current_version)
    }

    @Test fun testPacker(){
        val packerVersion = getCheckPointData("packer")
        assertNotNull(packerVersion.current_version)
    }
}