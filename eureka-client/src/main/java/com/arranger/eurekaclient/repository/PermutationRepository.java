package com.arranger.eurekaclient.repository;

import com.arranger.eurekaclient.entity.Permutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermutationRepository extends JpaRepository<Permutation, String> {

}
