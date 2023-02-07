package org.tuttleware.addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AddressBookTest {
    @Autowired
    private AddressBookRepository abRepository;

    @Autowired
    private BuddyInfoRepository biRepository;

    @Test
    public void testCreation() {
        AddressBook a = new AddressBook();
        assertEquals(0, a.getBuddies().size());
    }

    @Test
    public void testAddBuddy() {
        AddressBook a = new AddressBook();
        BuddyInfo b1 = new BuddyInfo("Sam", "613-555-1234");
        BuddyInfo b2 = new BuddyInfo("Ken", "123-456-7890");
        a.addBuddy(b1);
        a.addBuddy(b2);

        List<BuddyInfo> expected = new ArrayList<>();
        expected.add(b1);
        expected.add(b2);
        assertEquals(expected, a.getBuddies());
    }

    @Test
    public void testNonPersistedBuddy() {
        AddressBook a = new AddressBook();
        BuddyInfo b = new BuddyInfo("John", "613-555-4567");
        a.addBuddy(b);
        abRepository.save(a);

        Optional<AddressBook> book = abRepository.findById(a.getId());

        assertTrue(book.isPresent());
        assertEquals(a, book.get());

        Optional<BuddyInfo> buddy = biRepository.findById(b.getId());

        assertTrue(buddy.isPresent());
        assertEquals(b, buddy.get());
    }
}