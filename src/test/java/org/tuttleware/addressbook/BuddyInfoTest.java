package org.tuttleware.addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class BuddyInfoTest {
    @Autowired
    private BuddyInfoRepository repository;

    @Test
    public void testCreation() {
        BuddyInfo b = new BuddyInfo("Test", "613-000-0000");
        assertEquals("Test", b.getName());
        assertEquals("613-000-0000", b.getPhoneNumber());
    }

    @Test
    public void testToString() {
        BuddyInfo b = new BuddyInfo("Test", "613-000-0000");
        assertEquals("Test: 613-000-0000", b.toString());
    }

    @Test
    public void testUpdate() {
        BuddyInfo b = new BuddyInfo("Test", "613-000-0000");
        b.setName("Other");
        b.setPhoneNumber("613-555-1234");
        assertEquals("Other", b.getName());
        assertEquals("613-555-1234", b.getPhoneNumber());
    }

    @Test
    public void testPersistence() {
        BuddyInfo b = new BuddyInfo();
        b.setName("Peter");
        b.setPhoneNumber("613-888-9999");
        repository.save(b);

        Optional<BuddyInfo> buddy = repository.findById(b.getId());

        assertTrue(buddy.isPresent());
        assertEquals(b, buddy.get());
    }
}