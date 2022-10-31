package com.arranger.eurekaclient.dto;

import com.arranger.eurekaclient.entity.PermutationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogsDTO {

    private String id;
    private LocalDateTime startUpTime;
    private LocalDateTime shutDownTime;
    private Long executionTime;
    private String instanceId;
    private String userId;
    private PermutationStatus permutationStatus;
    private PermutationDTO permutation;
}
