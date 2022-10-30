package com.arranger.eurekaclient.repository;

import com.arranger.eurekaclient.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository<Logs, String> {
    Logs getLogsByPermutationId(String permutationId);
}
