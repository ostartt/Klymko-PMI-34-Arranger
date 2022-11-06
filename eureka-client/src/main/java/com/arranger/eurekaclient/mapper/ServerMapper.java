package com.arranger.eurekaclient.mapper;

import com.arranger.eurekaclient.dto.ServerDTO;
import com.arranger.eurekaclient.entity.Server;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ServerMapper {
    Server dtoToEntity(ServerDTO serverDTO);
    ServerDTO entityToDto(Server server);
}
