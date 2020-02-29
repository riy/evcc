package com.sherzad.evcc.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientResource {

    private String id;

    private String firstname;

    private String lastname;

    private String telephone;

    private String email;

    private String street;

    private String zipCode;

    private String city;

    private String country;

    private String status;
}
