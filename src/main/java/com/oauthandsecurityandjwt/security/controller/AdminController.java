package com.oauthandsecurityandjwt.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String mainP() {
        /*
            localhost에 대한 액세스가 거부됨
            이 페이지를 볼 수 있는 권한이 없습니다.
            HTTP ERROR 403
         */

        return "/security/admin";
    }
}
