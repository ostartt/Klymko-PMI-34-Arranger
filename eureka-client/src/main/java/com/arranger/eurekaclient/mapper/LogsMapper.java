package com.arranger.eurekaclient.mapper;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.entity.Logs;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface LogsMapper {
    Logs dtoToEntity(LogsDTO logsDTO);
    @Mapping(target = "userId", source = "user.id")
    LogsDTO entityToDto(Logs logs);
}
