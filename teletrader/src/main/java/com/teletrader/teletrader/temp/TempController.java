package com.teletrader.teletrader.temp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TempController {
    @GetMapping
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("All good!");
    }
}
