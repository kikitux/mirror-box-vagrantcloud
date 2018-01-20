package com.github.kikitux.mirrorboxvagrantcloud

import com.hashicorp.checkpoint.getCheckPointData
import com.hashicorp.releases.getRelease
import com.vagrantup.app.getApiUserData


fun main(args : Array<String>) {


    val product = "vagrant"
    println("calling checkpoint for ${product}")

    val version = getCheckPointData(product).current_version
    println("calling release for ${product}/${version}")

    val vagrantRelease = getRelease(product, version)

    val arch = "x86_64"

    var outDirectory = "mirror/${product}"

    print("\n\n")
    println("Downloading current version of vagrant binaries")
    downloadRelease(vagrantRelease, arch, outDirectory)

    print("\n\n")
    val user = "alvaro"

    println("calling VC api for user ${user}")
    val apiUser = getApiUserData(user)
    print("\n\n")


    val boxes = apiUser.boxes
            .filter { it.private == true }
            .filter { it.name == "precise64" }

    println("display some private boxes for user ${apiUser.username}") // from API call
    boxes.onEach {
        val box = it

        println(box.name)

        getFiles("mirror/test", box.name + ".box" , "https://vagrantcloud.com/alvaro/boxes/precise64/versions/0.5/providers/virtualbox.box")

    }

}

