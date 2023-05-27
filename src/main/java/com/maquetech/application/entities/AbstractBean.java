package com.maquetech.application.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;

@Getter
@Setter
@MappedSuperclass
public class AbstractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
}
