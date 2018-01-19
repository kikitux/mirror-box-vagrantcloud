package com.hashicorp.releases

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

data class Build(
    val name: String,
    val version: String,
    val os: String,
    val arch: String,
    val filename: String,
    val url: String
)

data class Release(
    val name: String,
    val version: String,
    val shasums: String,
    val shasums_signature: String,
    var builds: List<Build>
)

fun getRelease(product: String, version: String) : Release {

    val mapper = jacksonObjectMapper()
    val url = URL("https://releases.hashicorp.com/${product}/${version}/index.json")

    try {
        url.openConnection()
    } catch (e: java.io.FileNotFoundException) {
        println("can't access ${e.message}")
    }

    return mapper.readValue<Release>( url )

}