package com.sherzad.evcc.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientSearch {

    private String lastname;

    private String zipCode;

    private String city;

    private String country;

    @JsonIgnore
    private String status;
}
