package org.danilyuk.library.controllers;

import org.danilyuk.library.dao.BookDAO;
import org.danilyuk.library.dao.PersonDAO;
import org.danilyuk.library.models.Book;
import org.danilyuk.library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("book", bookDAO.index());
        return "book/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.show(id);
        model.addAttribute("book", book);
        if (book.getIdPerson() == 0) {
            model.addAttribute("people", personDAO.index());
        } else {
            model.addAttribute("person", personDAO.show(book.getIdPerson()));
        }
        return "book/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "book/new";

        bookDAO.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.show(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "book/edit";

        bookDAO.update(id, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/giveperson")
    public String givePerson(@PathVariable("id") int id, @ModelAttribute("book") Book book) {
        bookDAO.giveBook(id, book.getIdPerson());
        return "redirect:/book/" + id;
    }

    @PatchMapping("/{id}/freebook")
    public String givePerson(@PathVariable("id") int id) {
        bookDAO.freeBook(id);
        return "redirect:/book/" + id;
    }

}