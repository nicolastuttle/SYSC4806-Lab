package org.tuttleware.addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LabApplicationTests {
    @Autowired
    private AddressBookController abController;

    @Autowired
    private AddressBookService abService;

    @Test
    void contextLoads() {
        assertNotNull(abController);
        assertNotNull(abService);
    }

}
