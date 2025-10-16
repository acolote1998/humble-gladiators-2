package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Slf4j
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private Theme theme;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    byte[] coverImgBytes;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    byte[] cardBackImgBytes;

    @Enumerated(EnumType.STRING)
    private CampaignCreationStateType campaignCreationState = CampaignCreationStateType.STARTING_NEW_CAMPAIGN;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
