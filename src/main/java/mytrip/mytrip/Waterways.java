package mytrip.mytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Waterways {

    @GetMapping("/Waterways")
    public String getData() {
        return "Please book your ticket for your ship at 35% discount";
    }
}
