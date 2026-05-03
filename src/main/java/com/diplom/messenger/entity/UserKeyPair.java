package com.diplom.messenger.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_key_pairs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKeyPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String publicKeyPem;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String encryptedPrivateKeyPem;
}