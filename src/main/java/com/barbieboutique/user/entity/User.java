package com.barbieboutique.user.entity;

import com.barbieboutique.card.entity.Bucket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    private static final String SEQ_NAME = "user_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Bucket bucket;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Address address;

    @Column(name = "enabled")
    private boolean enabled;

    public User() {
        super();
        this.enabled = false;
    }
}

