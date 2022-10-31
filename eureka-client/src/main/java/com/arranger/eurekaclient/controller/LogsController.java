package com.arranger.eurekaclient.controller;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.service.LogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/logs")
@CrossOrigin(origins = "*")
public class LogsController {

    private final LogsService logsService;

    @Autowired
    public LogsController(LogsService logsService) {
        this.logsService = logsService;
    }

    @GetMapping("/{id}/permutations")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LogsDTO>
    getLogsByPermutationId(@PathVariable("id") String permutationId) {

        log.info("Getting logs by permutation id {}", permutationId);
        return ResponseEntity.status(HttpStatus.OK).body(
                logsService.getLogsByPermutationId(permutationId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LogsDTO>
    getLogsById(@PathVariable("id") String id) {

        log.info("Getting logs by id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(
                logsService.getLogsById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<LogsDTO>>
    getAllLogs(Pageable pageable) {

        log.info("Getting all logs");
        return ResponseEntity.status(HttpStatus.OK).body(
                logsService.getAllLogs(pageable));
    }
}
