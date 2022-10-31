package com.arranger.eurekaclient.controller;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.PermutationDTO;
import com.arranger.eurekaclient.dto.PermutationSaveDTO;
import com.arranger.eurekaclient.service.PermutationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/permutations")
@CrossOrigin(origins = "*")
public class PermutationController {

    private final PermutationService permutationService;

    public PermutationController(PermutationService permutationService) {
        this.permutationService = permutationService;
    }

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<CompletableFuture<LogsDTO>>
    runAndLogPermutation(@RequestBody @Valid PermutationSaveDTO permutation) throws InterruptedException {
        log.info("Running a permutation");

//        CompletableFuture.supplyAsync(() -> logsRepository.save(new Logs()));
        return ResponseEntity.status(HttpStatus.OK).body(
                permutationService.runAndLogPermutation(permutation));
    }

    @GetMapping("/{id}")
    //    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<PermutationDTO>
    getPermutationById(@PathVariable("id") String permutationId) {

        log.info("Getting a permutation by id {}", permutationId);
        return ResponseEntity.status(HttpStatus.OK).body(
                permutationService.getPermutationById(permutationId));
    }

    @PutMapping("/{id}")
    //    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Void>
    cancelTask(@PathVariable("id") String logsId) {
        log.info("Cancelling a task with id {}", logsId);

        permutationService.cancelTask(logsId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
