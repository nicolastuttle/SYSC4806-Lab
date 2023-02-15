package org.tuttleware.addressbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/addressBook")
public class AddressBookController {
    private final AddressBookService service;

    @Autowired
    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<AddressBook> books = this.service.list();
        model.addAttribute("books", books);
        return "addressBookList";
    }

    @PostMapping("/")
    public String create() {
        this.service.create();
        return "redirect:/addressBook/";
    }

    @GetMapping("/{bookId}")
    public String details(@PathVariable Integer bookId, Model model) {
        AddressBook book = this.service.details(bookId);

        model.addAttribute("book", book);
        return "details";
    }

    @GetMapping("/{bookId}/add")
    public String addBuddy(@PathVariable Integer bookId, Model model) {
        AddressBook book = this.service.details(bookId);
        if (book == null) {
            return "redirect:/addressBook/";
        }

        model.addAttribute("bookId", bookId);
        return "addBuddy";
    }

    @PostMapping("/{bookId}/add")
    public String addBuddy(@PathVariable Integer bookId, @RequestParam String name, @RequestParam String phoneNumber) {
        this.service.addBuddy(bookId, name, phoneNumber);
        return "redirect:/addressBook/" + bookId;
    }

    @PostMapping("/{bookId}/{buddyId}")
    public String deleteBuddy(@PathVariable Integer bookId, @PathVariable Integer buddyId) {
        this.service.deleteBuddy(bookId, buddyId);
        return "redirect:/addressBook/" + bookId;
    }
}
