package com.arranger.eurekaclient.repository;

import com.arranger.eurekaclient.entity.Server;
import com.arranger.eurekaclient.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<Server, String> {
}
