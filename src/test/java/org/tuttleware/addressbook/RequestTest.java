package org.tuttleware.addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressBookService service;

    @Test
    public void testHomePageNoBooks() throws Exception {
        this.mockMvc.perform(get("/addressBook/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There are no address books to display.")));
    }

    @Test
    public void testEmptyBookDetailsPage() throws Exception {
        when(service.details(1)).thenReturn(new AddressBook());
        this.mockMvc.perform(get("/addressBook/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There are no buddies associated with this book.")));
    }

    @Test
    public void testNoBookDetailsPage() throws Exception {
        this.mockMvc.perform(get("/addressBook/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("This book does not exist.")));
    }

    @Test
    public void testBooksHomePage() throws Exception {
        List<AddressBook> books = new ArrayList<>();
        AddressBook book = new AddressBook();
        book.setId(9999);
        books.add(book);
        when(service.list()).thenReturn(books);
        this.mockMvc.perform(get("/addressBook/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("9999")));
    }
}
