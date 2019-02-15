package org.auth1.auth1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    @RequestMapping("/status")
    public String index() {
        return "Hello world, I am online!";
    }
}
