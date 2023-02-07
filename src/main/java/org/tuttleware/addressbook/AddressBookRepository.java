package org.tuttleware.addressbook;

import org.springframework.data.repository.CrudRepository;

public interface AddressBookRepository extends CrudRepository<AddressBook, Integer> {
}
