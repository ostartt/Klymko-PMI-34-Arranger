package com.arranger.eurekaclient.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="SERVER")
public class Server {
    @Id
    String id;

    @Column(name = "task_number", nullable = false)
    @NonNull
    Integer taskNumber;

    @Column(name = "available_tasks")
    Integer availableTasks = 0;

    @Column(name = "tasks_run")
    Integer tasksRun = 0;

    @Column(name = "load_percent")
    Integer loadPercent = 0;
}
