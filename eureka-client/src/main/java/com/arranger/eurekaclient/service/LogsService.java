package com.arranger.eurekaclient.service;

import com.arranger.eurekaclient.dto.LogsAndNumberDTO;
import com.arranger.eurekaclient.dto.LogsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogsService {
    LogsDTO getLogsByPermutationId(String permutationId);

    LogsAndNumberDTO getAllLogs();

    LogsAndNumberDTO getAllLogsByUserEmail(Integer page, Integer amount, String userEmail);

    LogsDTO getLogsById(String id);
}
