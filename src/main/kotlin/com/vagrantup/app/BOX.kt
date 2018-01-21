package com.vagrantup.app

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


data class BOXProvider(
        var name: String?,
        var url: String?
)

data class BOXVersion(
        var version: String,
        var status: String,
        var description_html: String?,
        var description_markdown: String?,
        var providers: MutableList<BOXProvider>

)

data class BOX
(
        var description: String?,
        var short_description: String?,
        var name: String,
        var versions: List<BOXVersion>
)

// todo do it properly
fun newbox():BOX{

    val box = """{
    "description": "description",
    "short_description": "short_description",
    "name": "name",
    "versions": [
        {
            "version": "version",
            "status": "status",
            "description_html": "description_html",
            "description_markdown": "description_markdown",
            "providers": [
            ]
        }
    ]
}""".trimIndent()

    val mapper = jacksonObjectMapper()
    return mapper.readValue<BOX>(box)
}