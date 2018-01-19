package com.hashicorp.releases.tests

import com.hashicorp.checkpoint.getCheckPointData
import com.hashicorp.releases.getRelease
import org.junit.Test
import kotlin.test.assertNotNull


class ReleasesTests {
    @Test fun testVagrant(){
        val product = "vagrant"
        val version = getCheckPointData(product).current_version
        val vagrantRelease = getRelease(product, version)

        assertNotNull(vagrantRelease.builds)
        assertNotNull(vagrantRelease.builds.filter { it.os == "darwin" }.filter { it.arch == "x86_64" }[0].url)

    }
}