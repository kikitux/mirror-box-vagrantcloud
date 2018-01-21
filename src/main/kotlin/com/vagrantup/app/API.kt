package com.vagrantup.app

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import khttp.*




data class APIUserBoxProvider(
    val name: String,
    val hosted: Boolean,
    val hosted_token: Boolean?,
    val original_url: String?,
    val created_at: String,
    val updated_at: String,
    val download_url: String
)

data class APIUserBoxVersion(
    val version: String,
    val status: String,
    val description_html: String?,
    val description_markdown: String?,
    val created_at: String,
    val updated_at: String,
    val number: String,
    val release_url: String,
    val revoke_url: String,
    val providers: List<APIUserBoxProvider>
)

data class APIUserBox(
    val created_at: String,
    val updated_at: String,
    val tag: String,
    val name: String,
    val short_description: String?,
    val description_html: String?,
    val username: String,
    val description_markdown: String?,
    val private: Boolean,
    val current_version: APIUserBoxVersion?,
    val versions: List<APIUserBoxVersion>
)

data class APIUser(
    val username: String,
    val avatar_url: String,
    val profile_html: String?,
    val profile_markdown: String?,
    val boxes: List<APIUserBox>
)

fun getApiUserData(user: String): APIUser {

    val VAGRANT_CLOUD_TOKEN : String? = System.getenv("VAGRANT_CLOUD_TOKEN")

    val r = get("https://app.vagrantup.com/api/v1/user/${user}",
            headers=mapOf("Authorization" to "Bearer ${VAGRANT_CLOUD_TOKEN}"))

    val mapper = jacksonObjectMapper()
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)

    return mapper.readValue<APIUser>(r.text)
}