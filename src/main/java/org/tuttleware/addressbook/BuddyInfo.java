package org.tuttleware.addressbook;

import jakarta.persistence.*;

@Entity
public class BuddyInfo {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String phoneNumber;

    public BuddyInfo() {
    }

    public BuddyInfo(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BuddyInfo b)) {
            return false;
        }

        return this.name.equals(b.getName()) && this.phoneNumber.equals(b.getPhoneNumber());
    }
}
