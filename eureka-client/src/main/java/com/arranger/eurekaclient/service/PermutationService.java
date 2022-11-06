package com.arranger.eurekaclient.service;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.PermutationDTO;
import com.arranger.eurekaclient.dto.PermutationSaveDTO;
import com.arranger.eurekaclient.entity.Permutation;

import java.util.concurrent.CompletableFuture;

public interface PermutationService {

    void runPermutation(Permutation permutation, String logsId);

    PermutationDTO getPermutationById(String permutationId);

    CompletableFuture<Void> cancelTask(String permutationId);

    public CompletableFuture<LogsDTO> runAndLogPermutation(PermutationSaveDTO permutationSaveDTO,
                                                           String userId);
}
