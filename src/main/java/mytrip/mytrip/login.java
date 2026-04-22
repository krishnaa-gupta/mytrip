package mytrip.mytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class login {

    @GetMapping("/login")
    public String getData() {
        return "Please book your ticket for bus on 25% discount";
    }
}
