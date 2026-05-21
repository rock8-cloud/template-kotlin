package com.vibeham.template.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/info")
@Tag(name = "Info")
class InfoController {

    @GetMapping
    @Operation(summary = "Returns backend technology info")
    fun info() = mapOf("technology" to "Kotlin / Spring Boot / Gradle / PostgreSQL")
}
