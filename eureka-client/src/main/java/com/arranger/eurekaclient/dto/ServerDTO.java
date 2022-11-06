package com.arranger.eurekaclient.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ServerDTO implements Serializable {
    private String id;
    private Integer taskNumber;
    private Integer availableTasks;
    private Integer tasksRun;
    private String loadPercent;
}