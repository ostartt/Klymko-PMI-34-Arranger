package com.arranger.eurekaclient.controller;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.PermutationDTO;
import com.arranger.eurekaclient.dto.PermutationSaveDTO;
import com.arranger.eurekaclient.service.PermutationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/permutations")
public class PermutationController {

    private final PermutationService permutationService;

    public PermutationController(PermutationService permutationService) {
        this.permutationService = permutationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LogsDTO>
    runAndLogPermutation(@RequestBody @Valid PermutationSaveDTO permutation,
                         @NotNull Principal principal) {
        log.info("Running a permutation");

        return ResponseEntity.status(HttpStatus.OK).body(
                permutationService.runFirstStage(permutation, principal.getName()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PermutationDTO>
    getPermutationById(@PathVariable("id") String permutationId) {

        log.info("Getting a permutation by id {}", permutationId);
        return ResponseEntity.status(HttpStatus.OK).body(
                permutationService.getPermutationById(permutationId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Void>
    cancelTask(@PathVariable("id") String logsId) {
        log.info("Cancelling a task with id {}", logsId);

        permutationService.cancelTask(logsId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
