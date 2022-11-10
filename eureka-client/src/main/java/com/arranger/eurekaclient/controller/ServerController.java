package com.arranger.eurekaclient.controller;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.ServerDTO;
import com.arranger.eurekaclient.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/servers")
public class ServerController {

    private final ServerService serverService;

    @Autowired
    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ServerDTO>>
    getAllServers() {

        log.info("Getting all servers");
        return ResponseEntity.status(HttpStatus.OK).body(
                serverService.getAllServers());
    }

}
