package com.arranger.eurekaclient.controller;

import com.arranger.eurekaclient.dto.LogsAndNumberDTO;
import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.service.LogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;


@Slf4j
@RestController
@RequestMapping("/api/v1/logs")
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
    public ResponseEntity<LogsAndNumberDTO>
    getAllLogs() {

        log.info("Getting all logs");
        return ResponseEntity.status(HttpStatus.OK).body(
                logsService.getAllLogs());
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LogsAndNumberDTO>
    getAllLogsByUserEmail(@NotNull Principal principal,
                       @RequestParam("page") Integer page,
                       @RequestParam("amount") Integer amount) {

        log.info("Getting all logs by user email {}", principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                logsService.getAllLogsByUserEmail(page, amount, principal.getName()));
    }
}
