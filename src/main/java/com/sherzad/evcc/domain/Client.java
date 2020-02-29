package com.sherzad.evcc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CLIENT")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class Client {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;

    @NotNull(message = "Please enter a valid firstname.")
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;

    @NotNull(message = "Please enter a valid lastname.")
    @Column(name = "LASTNAME", nullable = false)
    private String lastname;

    @Column(name = "TELEPHONE")
    private String telephone;

    // The Standard validator for @Email considers addresses such as 'someone@somewhere' a valid email address. We therefore
    // have to come up with our own RegExp. In order to reuse this annotation elsewhere, and not specify the regexp each time,
    // one could wrap it in a custom Annotation.
    @Email(message = "Please enter a valid email address.", regexp = ".+@.+\\..+")
    @NotNull(message = "Please enter an email address.")
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "STREET")
    private String street;

    @Column(name = "ZIPCODE")
    private String zipCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "COUNTRY")
    private String country;

    // This could be a boolean, but if data storage is not an issue an Enum is always a better option in terms of extensibility.
    @NotNull(message = "Please specify a status.")
    @Column(name = "STATUS")
    @Setter
    private ClientStatusEnum status;
}
