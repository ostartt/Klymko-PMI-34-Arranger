package com.arranger.eurekaclient.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PermutationDTO {

    private String id;
    private String givenString;
    private Long permutationNumber;
    private Set<String> permutations;

}
