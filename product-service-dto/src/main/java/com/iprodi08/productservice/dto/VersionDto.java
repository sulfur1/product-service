package com.iprodi08.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class VersionDto {

    private String appVersion;

    private String appName;

    private LocalDateTime currentTime;

}
