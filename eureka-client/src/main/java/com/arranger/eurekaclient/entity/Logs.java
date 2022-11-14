package com.arranger.eurekaclient.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "LOGS")
public class Logs {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "start_up_time")
    private LocalDateTime startUpTime;

    @Column(name = "shut_down_time")
    private LocalDateTime shutDownTime;

    @Column(name = "execution_time")
    private Long executionTime;

    @Column(name = "instance_id")
    private String instanceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PermutationStatus permutationStatus = PermutationStatus.RAW;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // TODO
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "permutation_id", foreignKey = @ForeignKey(name = "fk_logs_permutation"))
    private Permutation permutation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_logs_user"))
    private User user;

}
