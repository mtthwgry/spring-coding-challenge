package com.outcomehealthtech.urlshortener.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
public class RequestObject {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String key;
}
