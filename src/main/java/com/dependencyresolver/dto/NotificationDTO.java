package com.dependencyresolver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
        private String feedName;
        private String message;
        private String cob;
        private String address;
        private String eventId;
        private String eventDateTime;

    }

