package com.github.kikitux.mirrorboxvagrantcloud

import com.hashicorp.releases.Release
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.Channels

import khttp.get
import khttp.responses.Response

// todo add test to this

fun getFiles(outDirectory: String, filename: String, url: String){
    val file = "${outDirectory}/${filename}"

    File(outDirectory).mkdirs()

    if (File(file).exists() && !File(file).isDirectory()) {
        println("#file exists ${file}")
    } else {
        println("#file doesn't exists ${file}")
        println("#Downloading " + url)
        try {

            var r: Response

            // if we got to those valid host, we set header with the token
            // empty if not defined

            if ( URL(url).host == "vagrantcloud.com" || URL(url).host == "app.vagrantup.com"){

                val VAGRANT_CLOUD_TOKEN : String? = System.getenv("VAGRANT_CLOUD_TOKEN")

                r = get(url, stream=true, allowRedirects = true,
                    headers=mapOf("Authorization" to "Bearer ${VAGRANT_CLOUD_TOKEN}"))

            } else {

                r = get(url, stream=true, allowRedirects = true)

            }

            var redirect = false

            // todo add counter and follow when redirect <3 use while ?
            // normally, 3xx is redirect
            val status = r.statusCode
            if (status != HttpURLConnection.HTTP_OK) {
                if (   status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER
                    || status == 307)
                    redirect = true
            }

            if (redirect) {

                // get redirect url from "location" header field
                val newUrl = r.connection.getHeaderField("Location")

                // open the new connnection again
                r = get(newUrl, stream=true, allowRedirects = true)
                println("#following redirect to ${URL(newUrl).host}")

            }

            val rbc = Channels.newChannel(r.raw)
            val fos = FileOutputStream(file)
            fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)
        } catch (e: java.io.FileNotFoundException) {
            println("#can't access ${e.message}")
        }

    }
}

fun downloadRelease(release: Release, arch: String, outDirectory: String){

    getFiles(outDirectory, release.shasums,
            "https://releases.hashicorp.com/${release.name}/${release.version}/${release.shasums}")

    getFiles(outDirectory, release.shasums_signature,
            "https://releases.hashicorp.com/${release.name}/${release.version}/${release.shasums_signature}")

    release.builds.filter { it.arch == arch }.onEach {
        getFiles(outDirectory, it.filename, it.url)
    }


}