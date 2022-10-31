package com.arranger.eurekaclient.entity;

import com.arranger.eurekaclient.validator.EmailConstraint;
import com.arranger.eurekaclient.validator.NameConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="USERS", indexes = {
        @Index(columnList = "email", name = "user_email_idx")
})
public class User {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(name="first_name", nullable=false)
    @NameConstraint
    private String firstName;

    @Column(name="last_name", nullable=false)
    @NameConstraint
    private String lastName;

    @Column(name="email", nullable=false, unique=true)
    @EmailConstraint
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="role", length=10, nullable=false)
    private Role role;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="is_active")
    private Boolean isActive = false;

    @CreatedDate
    @Column(name="create_date_time",  nullable=false)
    private LocalDateTime createDateTime;

    @LastModifiedDate
    @Column(name="update_date_time")
    private LocalDateTime updateDateTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @EqualsAndHashCode.Exclude
    private List<Logs> logs;

}