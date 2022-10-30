package com.arranger.eurekaclient.mapper;

import com.arranger.eurekaclient.dto.PermutationDTO;
import com.arranger.eurekaclient.entity.Permutation;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PermutationMapper {

    Permutation dtoToEntity(PermutationDTO permutationDTO);
    PermutationDTO entityToDto(Permutation permutation);
}
