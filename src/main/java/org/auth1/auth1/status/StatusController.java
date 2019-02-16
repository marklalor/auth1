package org.auth1.auth1.status;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.auth1.auth1.status.Status.Feature.*;

@RestController
public class StatusController {
    private static List<Status.Feature> availableFeatures = new ArrayList();
    private static List<Status.Feature> pendingFeatures = Arrays.asList(
            ENROLL,
            LOGIN_TOKEN,
            AUTH_TOKEN,
            MFA,
            RECOVER_PASSWORD
    );

    @RequestMapping("/status")
    public Status status(@RequestParam(value="name", defaultValue="guest") String name) {
        return new Status(name, LocalDateTime.now(), availableFeatures, pendingFeatures);
    }
}
