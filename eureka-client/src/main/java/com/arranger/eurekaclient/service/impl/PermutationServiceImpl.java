package com.arranger.eurekaclient.service.impl;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.PermutationDTO;
import com.arranger.eurekaclient.dto.PermutationSaveDTO;
import com.arranger.eurekaclient.entity.*;
import com.arranger.eurekaclient.exception.ResourcesExhaustedException;
import com.arranger.eurekaclient.mapper.LogsMapper;
import com.arranger.eurekaclient.mapper.PermutationMapper;
import com.arranger.eurekaclient.repository.LogsRepository;
import com.arranger.eurekaclient.repository.PermutationRepository;
import com.arranger.eurekaclient.repository.ServerRepository;
import com.arranger.eurekaclient.repository.UserRepository;
import com.arranger.eurekaclient.service.PermutationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private static final AtomicInteger maxProcessNumber = new AtomicInteger(3);
    private static final AtomicInteger processCounter = new AtomicInteger(0);
    private static final String maxProcessesMsg = String.format("You have reached max process number %s," +
            " please wait until processes finish", maxProcessNumber.get() * 3);

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @Autowired
    public PermutationServiceImpl(PermutationRepository permutationRepository,
                                  PermutationMapper permutationMapper,
                                  LogsRepository logsRepository,
                                  LogsMapper logsMapper,
                                  UserRepository userRepository,
                                  ServerRepository serverRepository) {
        this.permutationRepository = permutationRepository;
        this.permutationMapper = permutationMapper;
        this.logsRepository = logsRepository;
        this.logsMapper = logsMapper;
        this.userRepository = userRepository;
        this.serverRepository = serverRepository;
    }

    @Override
    public void cancelTask(String logsId) {
        log.info("Cancelling a task with id {}", logsId);

        logsRepository
                .findById(logsId)
                .map(logs ->
                {
                    logs.setPermutationStatus(PermutationStatus.INTERRUPTED);
                    return logsRepository.saveAndFlush(logs);

                })
                .orElseThrow(EntityNotFoundException::new);

        CompletableFuture.runAsync(() -> deleteLogs(logsId));
    }

    @Async
    public void deleteLogs(String logsId) {
        log.info("Deleting logs");
        logsRepository.flush();
        logsRepository.deleteById(logsId);

    }

    @Override
    public LogsDTO runFirstStage(PermutationSaveDTO permutationSaveDTO,
                                 String userEmail) {

        if (processCounter.get() >= maxProcessNumber.get()) {
            log.error(maxProcessesMsg);
            throw new ResourcesExhaustedException(maxProcessesMsg);
        }

        processCounter.incrementAndGet();
        log.info("Incrementing");

        LocalDateTime start = LocalDateTime.now();

        Permutation permutation = new Permutation();
        permutation.setGivenString(permutationSaveDTO.getGivenString());
        permutation.setPermutationNumber(getNumberOfPermutations(permutationSaveDTO.getGivenString()));

        Logs logs = new Logs();

        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(EntityNotFoundException::new);

        saveServer();

        logs.setUser(user);
        logs.setInstanceId(instanceId);
        logs.setPermutation(permutation);

        logsRepository.save(logs);
        permutationRepository.save(permutation);

        log.info("Logging a permutation");

        logs.setStartUpTime(start);
        CompletableFuture.runAsync(() -> runSecondStage(logs));
        return logsMapper.entityToDto(logsRepository.save(logs));
    }

    @Async
    public void runSecondStage(Logs logs) {

        runPermutation(logs.getPermutation(), logs);

        CompletableFuture.supplyAsync(() -> {
            logsMapper.entityToDto(logsRepository.save(logs));
            LocalDateTime stop = LocalDateTime.now();
            Long executionTime = ChronoUnit.SECONDS.between(logs.getStartUpTime(), stop);

            logs.setShutDownTime(stop);
            logs.setExecutionTime(executionTime);
            logs.setPermutationStatus(PermutationStatus.DONE);

            log.info("Decrementing");
            processCounter.decrementAndGet();

            saveServer();

            return logsMapper.entityToDto(logsRepository.save(logs)); // TODO: second stage
        });
    }

    @Override
    public void runPermutation(Permutation permutation, Logs logs) {
        log.info("Running a single permutation number {}", processCounter);

        permutation.setPermutations(permutationFinder(permutation.getGivenString()));
        permutationMapper.entityToDto(permutation);

    }

    @Override
    public PermutationDTO getPermutationById(String permutationId) {
        log.info("Getting a permutation by id {}", permutationId);

        return permutationMapper.entityToDto(permutationRepository
                .findById(permutationId)
                .orElseThrow(EntityNotFoundException::new));
    }

    private void saveServer() {
        log.info("Updating a server");

        Integer availableTasks = maxProcessNumber.get() - processCounter.get();
        String loadPercent = String.format("%.2f", ((processCounter.get() * 1.0 / maxProcessNumber.get()) * 100.0)) + "%";

        serverRepository.save(new Server(
                instanceId,
                maxProcessNumber.get(),
                availableTasks,
                processCounter.get(),
                loadPercent
        ));
    }

    private Long getNumberOfPermutations(String givenString) {
        log.debug("Calling permutation number algorithm");
        return LongStream.rangeClosed(1, givenString.length())
                .reduce(1, (long x, long y) -> x * y);
    }

    public Set<String> permutationFinder(String givenString) {
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

        Set<String> words = permutationFinder(rest);

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
