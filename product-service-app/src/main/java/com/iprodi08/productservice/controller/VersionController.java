package com.iprodi08.productservice.controller;

import com.iprodi08.productservice.dto.VersionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api")
public class VersionController {

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.name}")
    private String appName;

    @Operation(
            summary = "Get app information",
            description = "Get app name, version"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @GetMapping("/info")
    public VersionDto welcome() {
        return new VersionDto(appVersion, appName, LocalDateTime.now());
    }

}
