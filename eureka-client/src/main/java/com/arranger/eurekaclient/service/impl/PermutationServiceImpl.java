package com.arranger.eurekaclient.service.impl;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.PermutationDTO;
import com.arranger.eurekaclient.dto.PermutationSaveDTO;
import com.arranger.eurekaclient.entity.Logs;
import com.arranger.eurekaclient.entity.Permutation;
import com.arranger.eurekaclient.entity.PermutationStatus;
import com.arranger.eurekaclient.mapper.LogsMapper;
import com.arranger.eurekaclient.mapper.PermutationMapper;
import com.arranger.eurekaclient.repository.LogsRepository;
import com.arranger.eurekaclient.repository.PermutationRepository;
import com.arranger.eurekaclient.service.PermutationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

@Slf4j
@Service
public class PermutationServiceImpl implements PermutationService {

    private final PermutationRepository permutationRepository;
    private final PermutationMapper permutationMapper;
    private final LogsRepository logsRepository;
    private final LogsMapper logsMapper;
    private static final AtomicInteger maxProcessNumber = new AtomicInteger(1);
    private static final AtomicInteger processCounter = new AtomicInteger(0);
    private static final String maxProcessesMsg = String.format("You have reached max process number %s," +
            " please wait until processes finish", maxProcessNumber);

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @Autowired
    public PermutationServiceImpl(PermutationRepository permutationRepository,
                                  PermutationMapper permutationMapper,
                                  LogsRepository logsRepository,
                                  LogsMapper logsMapper) {
        this.permutationRepository = permutationRepository;
        this.permutationMapper = permutationMapper;
        this.logsRepository = logsRepository;
        this.logsMapper = logsMapper;
    }

    @Override
    @Async
    public CompletableFuture<Void> cancelTask(String logsId) {
        log.info("Cancelling a task with id {}", logsId);

        log.info("Decrementing from cancel");
        processCounter.decrementAndGet();

        return CompletableFuture.runAsync(() -> logsRepository
                .findById(logsId)
                .map(logs ->
                {
                    logs.setPermutationStatus(PermutationStatus.INTERRUPTED);
                    return logsRepository.save(logs);

                })
                .orElseThrow(EntityNotFoundException::new))
                .thenApply(action -> {
                        log.info("Deleting logs");
                        logsRepository.deleteById(logsId);
                    return action;
                });
    }

    @Override
    @Async
    public CompletableFuture<LogsDTO> runAndLogPermutation(PermutationSaveDTO permutationSaveDTO) {

        if (processCounter.get() >= maxProcessNumber.get()) {
            log.error(maxProcessesMsg);
            throw new ResourceAccessException(maxProcessesMsg);
        }

        processCounter.incrementAndGet();
        log.info("Incrementing");

        LocalDateTime start = LocalDateTime.now();

        Permutation permutation = new Permutation();
        permutation.setGivenString(permutationSaveDTO.getGivenString());
        permutation.setPermutationNumber(getNumberOfPermutations(permutationSaveDTO.getGivenString()));

        Logs logs = new Logs();
        logs.setInstanceId(instanceId);
        logs.setPermutation(permutation);

        logsRepository.save(logs);
        permutationRepository.save(permutation);

        log.info("Logging a permutation");

        runPermutation(permutation, logs.getId());

        logs.setStartUpTime(start);

        return CompletableFuture.supplyAsync(() -> {
                    logsMapper.entityToDto(logsRepository.save(logs));
                    LocalDateTime stop = LocalDateTime.now();
                    Long executionTime = ChronoUnit.SECONDS.between(start, stop);

                    logs.setShutDownTime(stop);
                    logs.setExecutionTime(executionTime);
                    logs.setPermutationStatus(PermutationStatus.DONE); // TODO:

                    log.info("Decrementing");
                    processCounter.decrementAndGet();
                    return logsMapper.entityToDto(logsRepository.save(logs));
                });
    }

    @Override
    public void runPermutation(Permutation permutation, String logsId) {
        log.info("Running a single permutation number {}", processCounter);

        permutation.setPermutations(permutationFinder(permutation.getGivenString(), logsId));
        permutationMapper.entityToDto(permutation);

    }

    @Override
    public PermutationDTO getPermutationById(String permutationId) {
        log.info("Getting a permutation by id {}", permutationId);

        return permutationMapper.entityToDto(permutationRepository
                .findById(permutationId)
                .orElseThrow(EntityNotFoundException::new));
    }

    private Long getNumberOfPermutations(String givenString) {
        log.debug("Calling permutation number algorithm");
        return LongStream.rangeClosed(1, givenString.length())
                .reduce(1, (long x, long y) -> x * y);
    }

    public Set<String> permutationFinder(String givenString, String logsId) {
        log.debug("Calling permutation algorithm");

        Set<String> permutations = new HashSet<>();

        if (givenString == null) {
            return null;
        } else if (givenString.length() == 0) {
            permutations.add("");
            return permutations;
        }

        char initial = givenString.charAt(0);
        String rest = givenString.substring(1);

        Set<String> words = permutationFinder(rest, logsId);

        assert words != null;
        for (String newString : words) {
            for (int i = 0; i <= newString.length(); i++) {
                permutations.add(charInsert(newString, initial, i));
            }
        }
        return permutations;
    }

    private String charInsert(String str, char singleChar, int index) {
        log.debug("Calling char insertion algorithm");

        String begin = str.substring(0, index);
        String end = str.substring(index);

        return begin + singleChar + end;
    }
}