package com.arranger.eurekaclient.service.impl;

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
    public List<LogsDTO> getAllLogs(Pageable pageable){
        log.info("Getting all logs");

        Page<Logs> logsPage = logsRepository
                .findAll(PageRequest.of(0,6));

        return logsPage
                .stream()
                .map(logsMapper::entityToDto)
                .collect(Collectors.toList());
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
