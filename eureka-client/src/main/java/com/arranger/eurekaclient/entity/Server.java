package com.arranger.eurekaclient.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="SERVER")
public class Server {
    @Id
    @NonNull
    String id;

    @Column(name = "task_number", nullable = false)
    @NonNull
    Integer taskNumber;

    @Column(name = "available_tasks")
    @NonNull
    Integer availableTasks = 0;

    @Column(name = "tasks_run")
    @NonNull
    Integer tasksRun = 0;

    @Column(name = "load_percent")
    @NonNull
    String loadPercent = "0.0";
}
