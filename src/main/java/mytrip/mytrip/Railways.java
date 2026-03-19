package mytrip.mytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Railways {

    @GetMapping("/Railways")
    public String getData() {
        return "Please book your ticket on 70% discount";
    }
}
