const AddressBookManagement = {
    deleteBook: () => {
        const bookId = $("#book-id").val();
        $.ajax({
            type: "DELETE",
            dataType: "json",
            url: `/api/addressBook/${bookId}`,
            success: () => {
                // Refresh list
                AddressBookManagement.loadAddressBooks();
            }
        });

        $("#book-display").slideUp();
        return false;
    },
    confirmAdd: () => {
        const name = $("#buddy-name").val();
        const phoneNumber = $("#buddy-phone-number").val();
        const bookId = $("#book-id").val()
        $.ajax({
            type: "POST",
            dataType: "json",
            url: `/api/addressBook/${bookId}`,
            data: {
                name,
                phoneNumber,
            },
            success: () => {
                // Refresh list and book details
                AddressBookManagement.loadAddressBooks();
                AddressBookManagement.loadBookById({ target: { text: bookId }});
            }
        });

        // Hide form
        $("#buddy-details").hide()
        return false;
    },
    addBuddy: () => {
        // Reset and show form
        $("#buddy-name").val("");
        $("#buddy-phone-number").val("");
        $("#buddy-details").show()
        return false;
    },
    deleteBuddy: e => {
        const buttonId = e.target.id;
        const [_, bookId, buddyId] = buttonId.split("-");
        $.ajax({
            type: "DELETE",
            dataType: "json",
            url: `/api/addressBook/${bookId}/${buddyId}`,
            success: () => {
                // Refresh list and book details
                AddressBookManagement.loadAddressBooks();
                AddressBookManagement.loadBookById({ target: { text: bookId }});
            }
        });
        return false;
    },
    displayBook: book => {
        $("#book-id").val(book.id);
        if (book["buddies"].length === 0) {
            $("#no-buddies-message").show();
            $("#buddy-table").hide();
        } else {
            $("#no-buddies-message").hide();
            $("#buddy-table").show();

            // Generate new table body
            const buddyList = book["buddies"].map(buddy => {
                const newRow = $("<tr></tr>");

                newRow.append(`<td>${buddy.name}</td>`);
                newRow.append(`<td>${buddy.phoneNumber}</td>`);
                newRow.append(`<td><button id="delete-${book.id}-${buddy.id}" class="delete-buddy">Delete</button></td>`);

                return newRow;
            });

            $("#buddy-list").html(buddyList);
        }
        $("#book-title").text(`Address book #${book.id}`);
        $("#book-display").slideDown();
    },
    loadBookById: e => {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: `/api/addressBook/${e.target.text}`,
            success: AddressBookManagement.displayBook
        });
        return false;
    },
    displayAddressBooks: books => {
        if (books.length === 0) {
            $("#book-table").hide();
            $("#no-book-message").show();
        } else {
            $("#book-table").show();
            $("#no-book-message").hide();

            // Generate new table body
            const bookList = books.map(book => {
                const newRow = $("<tr></tr>")
                newRow.append(`<td><a href="${book.id}">${book.id}</a></td>`);

                const bookSize = book["buddies"].length;
                const plural = bookSize === 1 ? "entry" : "entries";
                newRow.append(`<td>${bookSize} ${plural}</td>`);

                return newRow;
            });

            $("#address-book-list").html(bookList);
        }
    },
    loadAddressBooks: () => {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "/api/addressBook",
            success: AddressBookManagement.displayAddressBooks
        });
    },
    createAddressBook: () => {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/api/addressBook",
            success: AddressBookManagement.loadAddressBooks
        });
        return false;
    },
    setup: () => {
        $("#new-address-book").click(AddressBookManagement.createAddressBook);
        $("#book-display").hide().removeClass("hidden").addClass("book-details");
        $(document).on("click", "a", AddressBookManagement.loadBookById);
        $(document).on("click", ".delete-buddy", AddressBookManagement.deleteBuddy);
        const buddyDetails = $("#buddy-details");
        buddyDetails.hide();
        $("#add-buddy").click(AddressBookManagement.addBuddy);
        $("#confirm-add").click(AddressBookManagement.confirmAdd);
        $("#cancel-add").click(() => {
            buddyDetails.hide();
            return false;
        });
        $("#delete-book").click(AddressBookManagement.deleteBook);
    }
}

$(AddressBookManagement.setup);
