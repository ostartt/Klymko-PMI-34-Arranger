package com.arranger.eurekaclient.service;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.PermutationDTO;
import com.arranger.eurekaclient.dto.PermutationSaveDTO;
import com.arranger.eurekaclient.entity.Logs;
import com.arranger.eurekaclient.entity.Permutation;

import java.util.concurrent.CompletableFuture;

public interface PermutationService {

    void runPermutation(Permutation permutation, Logs logs);

    PermutationDTO getPermutationById(String permutationId);

    void cancelTask(String permutationId);

    LogsDTO runFirstStage(PermutationSaveDTO permutationSaveDTO,
                                              String userId);
}
