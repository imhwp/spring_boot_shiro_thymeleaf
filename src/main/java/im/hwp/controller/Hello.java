package im.hwp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("hello")
public class Hello {

    @RequestMapping("hello")
    public String hello(){
        System.out.println("hello world");
        return "index";
    }
}
