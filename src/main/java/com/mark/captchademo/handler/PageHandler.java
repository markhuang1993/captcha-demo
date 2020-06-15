package com.mark.captchademo.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageHandler {

    @RequestMapping("/{pName}")
    public String dispatchPage(@PathVariable final String pName) {
        return pName;
    }
}
