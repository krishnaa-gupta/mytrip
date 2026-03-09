package mytrip.mytrip;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Airways {

    @GetMapping("/Airays")
    public String getData() {
        return "Please book your flight ticket on 25% discount";
    }
}
