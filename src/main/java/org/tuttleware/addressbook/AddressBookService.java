package org.tuttleware.addressbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addressBook")
public class AddressBookService {
    private final AddressBookRepository repo;

    @Autowired
    public AddressBookService(AddressBookRepository repo) {
        this.repo = repo;
    }

    @GetMapping("")
    public List<AddressBook> list() {
        List<AddressBook> books = new ArrayList<>();
        this.repo.findAll().iterator().forEachRemaining(books::add);
        return books;
    }

    @PostMapping("")
    public Integer create() {
        AddressBook a = new AddressBook();
        this.repo.save(a);
        return a.getId();
    }

    @GetMapping("/{bookId}")
    public AddressBook details(@PathVariable Integer bookId) {
        Optional<AddressBook> a = this.repo.findById(bookId);
        return a.orElse(null);
    }

    @PostMapping("/{bookId}")
    public Boolean addBuddy(@PathVariable Integer bookId, @RequestParam String name, @RequestParam String phoneNumber) {
        Optional<AddressBook> o = this.repo.findById(bookId);
        if (o.isEmpty()) {
            return false;
        }

        AddressBook a = o.get();
        a.addBuddy(new BuddyInfo(name, phoneNumber));
        this.repo.save(a);
        return true;
    }

    @DeleteMapping("/{bookId}")
    public Boolean deleteBook(@PathVariable Integer bookId) {
        Optional<AddressBook> o = this.repo.findById(bookId);
        if (o.isPresent()) {
            this.repo.delete(o.get());
            return true;
        }
        return false;
    }

    @DeleteMapping("/{bookId}/{buddyId}")
    public Boolean deleteBuddy(@PathVariable Integer bookId, @PathVariable Integer buddyId) {
        Optional<AddressBook> o = this.repo.findById(bookId);
        if (o.isEmpty()) {
            return false;
        }

        boolean modified = false;
        AddressBook a = o.get();
        Iterator<BuddyInfo> buddies = a.getBuddies().iterator();
        while (buddies.hasNext()) {
            BuddyInfo buddy = buddies.next();
            if (buddy.getId() == buddyId) {
                buddies.remove();
                modified = true;
            }
        }

        this.repo.save(a);
        return modified;
    }
}
