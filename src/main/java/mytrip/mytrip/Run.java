package mytrip.mytrip;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Run {

    @GetMapping("/Run")
    public String getData() {
        return "Please book your ticket for marathon at 25% discount";
    }
}

