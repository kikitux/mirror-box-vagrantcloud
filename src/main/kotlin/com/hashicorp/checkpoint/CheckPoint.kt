package com.hashicorp.checkpoint

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

data class CheckPointData(
        val product: String,
        val current_version: String,
        val current_release: String,
        val current_download_url: String,
        val current_changelog_url: String,
        val project_website: String,
        val alerts: List<String>
)

fun getCheckPointData(product: String): CheckPointData {
    val url = URL("https://checkpoint-api.hashicorp.com/v1/check/${product}")

    try {
        url.openConnection()
    } catch (e: java.io.FileNotFoundException) {
        println("can't access ${e.message}")
    }

    val mapper = jacksonObjectMapper()

    return mapper.readValue<CheckPointData>(url)
}

