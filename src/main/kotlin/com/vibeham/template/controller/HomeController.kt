package com.vibeham.template.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping("/")
    fun home() = "redirect:/swagger-ui/index.html"
}
