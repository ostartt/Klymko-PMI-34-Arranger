package com.arranger.eurekaclient.dto;

import com.arranger.eurekaclient.validator.PermutationConstraint;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PermutationSaveDTO {

    @NotNull
    @PermutationConstraint
    String givenString;
}
