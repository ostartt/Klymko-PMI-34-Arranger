package com.arranger.eurekaclient.repository;

import com.arranger.eurekaclient.entity.Logs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogsRepository extends JpaRepository<Logs, String> {
    Logs getLogsByPermutationId(String permutationId);
    Optional<List<Logs>> getLogsByUserEmail(String userEmail, Pageable pageable);
}
