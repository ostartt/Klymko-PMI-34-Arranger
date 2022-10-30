package com.arranger.eurekaclient.mapper;

import com.arranger.eurekaclient.dto.LogsDTO;
import com.arranger.eurekaclient.entity.Logs;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface LogsMapper {
    Logs dtoToEntity(LogsDTO logsDTO);
    LogsDTO entityToDto(Logs logs);
}
