package com.arranger.eurekaclient.service;

import com.arranger.eurekaclient.dto.LogsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogsService {
    LogsDTO getLogsByPermutationId(String permutationId);

    List<LogsDTO> getAllLogs(Pageable pageable);

    List<LogsDTO> getAllLogsByUserId(Pageable pageable, String userId);

    LogsDTO getLogsById(String id);
}
