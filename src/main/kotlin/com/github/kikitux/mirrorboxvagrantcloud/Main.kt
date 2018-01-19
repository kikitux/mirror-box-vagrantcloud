package com.github.kikitux.mirrorboxvagrantcloud

import com.hashicorp.checkpoint.getCheckPointData
import com.hashicorp.releases.getRelease


fun main(args : Array<String>) {

    val product = "vagrant"
    val version = getCheckPointData(product).current_version
    val vagrantRelease = getRelease(product, version)

    val arch = "x86_64"

    val outDirectory = "mirror/vagrant"

    downloadRelease(vagrantRelease, arch, outDirectory)





}

