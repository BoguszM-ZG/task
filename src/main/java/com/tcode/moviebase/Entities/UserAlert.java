package com.tcode.moviebase.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_alert", schema = "movies")
public class UserAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Size(max = 255)
    @Column(name = "message")
    private String message;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "`read`", nullable = false)
    private Boolean read = false;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    public UserAlert(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}