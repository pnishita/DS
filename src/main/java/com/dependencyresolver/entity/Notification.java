package com.dependencyresolver.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
@Builder
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;
    private String message;
    private LocalDate cob;
    private String address;
    private String eventId;
    private LocalDateTime eventDateTime;

}

