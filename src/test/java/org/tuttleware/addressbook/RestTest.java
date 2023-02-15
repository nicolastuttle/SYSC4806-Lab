package org.tuttleware.addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTest {
    @Autowired
    private AddressBookRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final int UNUSED_ID = 99999999;

    @Test
    public void testGetAddressBookList() {
        for (int i = 0; i < 5; i++) {
            AddressBook a = new AddressBook();
            repository.save(a);
        }

        List<AddressBook> books = new ArrayList<>();
        repository.findAll().forEach(books::add);
        ResponseEntity<AddressBook[]> response = restTemplate.getForEntity("/api/addressBook", AddressBook[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        for (AddressBook a : response.getBody()) {
            assertTrue(books.contains(a));
        }
    }

    @Test
    public void testCreateAddressBook() {
        ResponseEntity<Integer> response = restTemplate.postForEntity("/api/addressBook", null, Integer.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);
    }

    @Test
    public void testGetAddressBook() {
        AddressBook a = new AddressBook();
        a.addBuddy(new BuddyInfo("Brad", "613-555-1234"));
        repository.save(a);

        ResponseEntity<AddressBook> response = restTemplate.getForEntity(
                "/api/addressBook/" + a.getId(),
                AddressBook.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(a, response.getBody());
    }

    @Test
    public void testGetNonExistentAddressBook() {
        ResponseEntity<AddressBook> response = restTemplate.getForEntity(
                "/api/addressBook/" + UNUSED_ID,
                AddressBook.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddBuddyToBook() {
        AddressBook a = new AddressBook();
        repository.save(a);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Ricky Bobby");
        params.add("phoneNumber", "613-123-1234");

        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "/api/addressBook/" + a.getId(),
                params,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        BuddyInfo b = new BuddyInfo("Ricky Bobby", "613-123-1234");
        a = repository.findById(a.getId()).orElseThrow();
        assertTrue(a.getBuddies().contains(b));
    }

    @Test
    public void testAddBuddyToNonExistentBook() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Ricky Bobby");
        params.add("phoneNumber", "613-123-1234");

        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "/api/addressBook/" + UNUSED_ID,
                params,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
    }

    @Test
    public void testDeleteBook() {
        AddressBook a = new AddressBook();
        repository.save(a);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/addressBook/" + a.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        assertTrue(repository.findById(a.getId()).isEmpty());
    }

    @Test
    public void testDeleteNotExistentBook() {
        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/addressBook/" + UNUSED_ID,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
    }

    @Test
    public void testDeleteBuddy() {
        AddressBook a = new AddressBook();
        BuddyInfo b = new BuddyInfo("Bubbles", "123-456-7890");
        a.addBuddy(b);
        repository.save(a);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/addressBook/" + a.getId() + "/" + b.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        a = repository.findById(a.getId()).orElseThrow();
        assertFalse(a.getBuddies().contains(b));
    }

    @Test
    public void testDeleteNonExistentBuddy() {
        AddressBook a = new AddressBook();
        repository.save(a);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/addressBook/" + a.getId() + "/" + UNUSED_ID,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
    }

    @Test
    public void testDeleteBuddyFromNonExistentBook() {
        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/addressBook/" + UNUSED_ID + "/1",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
    }
}
