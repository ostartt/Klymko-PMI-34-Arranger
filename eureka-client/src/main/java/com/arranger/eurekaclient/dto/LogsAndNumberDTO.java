package com.arranger.eurekaclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class LogsAndNumberDTO {

    @NotNull
    List<LogsDTO> logsDTOList;

    @NotNull
    Long logsNumber;
}
