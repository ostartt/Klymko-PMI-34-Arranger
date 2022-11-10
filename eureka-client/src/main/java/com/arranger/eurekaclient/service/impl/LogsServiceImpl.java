package com.arranger.eurekaclient.service.impl;

import com.arranger.eurekaclient.dto.LogsAndNumberDTO;
import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.entity.Logs;
import com.arranger.eurekaclient.mapper.LogsMapper;
import com.arranger.eurekaclient.repository.LogsRepository;
import com.arranger.eurekaclient.service.LogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class LogsServiceImpl implements LogsService {
    private final LogsRepository logsRepository;
    private final LogsMapper logsMapper;

    @Autowired
    public LogsServiceImpl(LogsRepository logsRepository,
                           LogsMapper logsMapper) {
        this.logsRepository = logsRepository;
        this.logsMapper = logsMapper;
    }

    @Override
    public LogsDTO getLogsByPermutationId(String permutationId) {
        log.info("Getting logs by permutation id {}", permutationId);

        return logsMapper.entityToDto(logsRepository.getLogsByPermutationId(permutationId));
    }

    @Override
    public LogsAndNumberDTO getAllLogs() {
        log.info("Getting all logs");

        List<Logs> logs = logsRepository
                .findAll();

        Long logsNumber = logsRepository.count();

        List<LogsDTO> logsDTO = logs
                .stream()
                .map(logsMapper::entityToDto).toList();

        return new LogsAndNumberDTO(logsDTO, logsNumber);
    }

    @Override
    public LogsAndNumberDTO getAllLogsByUserEmail(Integer page, Integer amount, String userEmail) {
        log.info("Getting all logs by user id {}", userEmail);

        List<Logs> logs = logsRepository
                .getLogsByUserEmail(userEmail, PageRequest.of(page, amount))
                .orElseThrow(EntityNotFoundException::new);

        Long logsNumber = logsRepository.count();

        List<LogsDTO> logsDTO = logs
                .stream()
                .map(logsMapper::entityToDto).toList();

        return new LogsAndNumberDTO(logsDTO, logsNumber);
    }

    @Override
    public LogsDTO getLogsById(String id) {
        log.info("Getting logs by id {}", id);

        return logsMapper.entityToDto(
                logsRepository
                        .findById(id)
                        .orElseThrow(EntityNotFoundException::new)
        );
    }

}
