package com.sherzad.evcc.common;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Profiles {

    /**
     * Profile that activates the creation of test data during application startup
     */
    public static final String POPULATE = "populate";
}
