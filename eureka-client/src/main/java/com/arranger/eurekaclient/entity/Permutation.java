package com.arranger.eurekaclient.entity;


import com.arranger.eurekaclient.validator.PermutationConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PERMUTATION")
public class Permutation {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "given_string", nullable = false)
    @PermutationConstraint
    private String givenString;

    @ElementCollection
    @CollectionTable(name = "permutation_list")
    private Set<String> permutations;

    @Column(name = "permutation_number", nullable = true)
    private Long permutationNumber;
}
