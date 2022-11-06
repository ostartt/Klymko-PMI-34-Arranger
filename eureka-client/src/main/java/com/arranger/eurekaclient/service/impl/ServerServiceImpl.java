package com.arranger.eurekaclient.service.impl;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.dto.ServerDTO;
import com.arranger.eurekaclient.entity.Logs;
import com.arranger.eurekaclient.entity.Server;
import com.arranger.eurekaclient.mapper.ServerMapper;
import com.arranger.eurekaclient.repository.ServerRepository;
import com.arranger.eurekaclient.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerMapper serverMapper;
    private final ServerRepository serverRepository;

    @Autowired
    public ServerServiceImpl(ServerMapper serverMapper, ServerRepository serverRepository) {
        this.serverMapper = serverMapper;
        this.serverRepository = serverRepository;
    }
    @Override
    public List<ServerDTO> getAllServers() {
        log.info("Getting all servers");

        List<Server> servers = serverRepository
                .findAll();

        return servers
                .stream()
                .map(serverMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
