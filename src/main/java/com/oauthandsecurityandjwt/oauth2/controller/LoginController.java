package com.oauthandsecurityandjwt.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {

        // 이제 로그인 페이지가 이것으로 대체됨
        return "login";
    }
}
