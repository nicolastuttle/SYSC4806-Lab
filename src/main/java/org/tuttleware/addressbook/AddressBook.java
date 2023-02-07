package org.tuttleware.addressbook;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AddressBook {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BuddyInfo> buddies;

    public AddressBook() {
        this.buddies = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public List<BuddyInfo> getBuddies() {
        return buddies;
    }

    public void addBuddy(BuddyInfo buddy) {
        this.buddies.add(buddy);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBuddies(List<BuddyInfo> buddies) {
        this.buddies = buddies;
    }

    public String oldToString() {
        StringBuilder sb = new StringBuilder("=== ADDRESS BOOK ENTRIES ===\n");
        for (BuddyInfo buddy : buddies) {
            sb.append(buddy.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        int size = this.buddies.size();
        String plural = size == 1 ? "entry" : "entries";
        return size + " " + plural;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AddressBook a) || a.buddies.size() != this.buddies.size()) {
            return false;
        }

        for (int i = 0; i < this.buddies.size(); i++) {
            if (!this.buddies.get(i).equals(a.buddies.get(i))) {
                return false;
            }
        }

        return true;
    }
}
