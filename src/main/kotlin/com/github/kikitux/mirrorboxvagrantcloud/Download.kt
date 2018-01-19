package com.github.kikitux.mirrorboxvagrantcloud

import com.hashicorp.releases.Release
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

// todo add test to this

fun getFiles(outDirectory: String, filename: String, url: String){
    val file = "${outDirectory}/${filename}"

    if (File(file).exists() && !File(file).isDirectory()) {
        println("file exists ${file}")
    } else {
        println("file doesn't exists ${file}")
        println("Downloading " + url)
        try {
            val website = URL(url)
            val rbc = Channels.newChannel(website.openStream())
            val fos = FileOutputStream(file)
            fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)
        } catch (e: java.io.FileNotFoundException) {
            println("can't access ${e.message}")
        }

    }
}

fun downloadRelease(release: Release, arch: String, outDirectory: String){

    File(outDirectory).mkdirs()

    getFiles(outDirectory, release.shasums,
            "https://releases.hashicorp.com/${release.name}/${release.version}/${release.shasums}")

    getFiles(outDirectory, release.shasums_signature,
            "https://releases.hashicorp.com/${release.name}/${release.version}/${release.shasums_signature}")

    release.builds.filter { it.arch == arch }.onEach {
        getFiles(outDirectory, it.filename, it.url)
    }


}