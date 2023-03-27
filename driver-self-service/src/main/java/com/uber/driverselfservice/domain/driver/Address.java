package com.uber.driverselfservice.domain.driver;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A Address is a value object that is used to represent the Address of a driver.
 * */
@Entity
@Table(name = "addresses")
public class Address {
    @GeneratedValue
    @Id
    private Long id;

    private final String locality;

    private final String pinCode;

    private final String city;

    public Address() {
        this.locality = null;
        this.pinCode = null;
        this.city = null;
    }

    public Address(String locality, String pinCode, String city) {
        this.locality = locality;
        this.pinCode = pinCode;
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return String.format("%s, %s %ss", locality, pinCode, city);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Address other = (Address) obj;
        return Objects.equals(locality, other.locality) &&
                Objects.equals(pinCode, other.pinCode) &&
                Objects.equals(city, other.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locality, pinCode, city);
    }
}