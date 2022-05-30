package org.danilyuk.library.controllers;

import org.danilyuk.library.models.Book;
import org.danilyuk.library.models.Person;
import org.danilyuk.library.services.BookService;
import org.danilyuk.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.PersistenceDelegate;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        if (page == null || booksPerPage == null) {
            model.addAttribute("book", bookService.findAll(sortByYear));
        } else {
            model.addAttribute("book", bookService.findWithPagination(sortByYear, page, booksPerPage));
        }
        return "book/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.show(id));
        Person bookOwner  = bookService.getBookOwner(id);
        if (bookOwner != null) {
            model.addAttribute("owner",bookOwner);
        } else {
            model.addAttribute("people", peopleService.index());
        }
        return "book/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "book/new";

        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.show(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "book/edit";

        bookService.update(id, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/assign")
    public String givePerson(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.giveBook(id, selectedPerson);
        return "redirect:/book/" + id;
    }

    @PatchMapping("/{id}/release")
    public String freeBook(@PathVariable("id") int id) {
        bookService.freeBook(id);
        return "redirect:/book/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "book/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.searchByTitle(query));
        return "book/search";
    }

}