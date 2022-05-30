package org.danilyuk.library.services;

import org.danilyuk.library.models.Book;
import org.danilyuk.library.models.Person;
import org.danilyuk.library.repositories.BookRepository;
import org.danilyuk.library.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by("year"));

        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findWithPagination(boolean sortByYear, Integer page, Integer booksPerPage) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page,booksPerPage,Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page,booksPerPage)).getContent();
        }
    }

    public Book show(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(updatedBook.getId()).get();
        updatedBook.setId(id);
        updatedBook.setPerson(bookToBeUpdated.getPerson());
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void freeBook(int id) {
        bookRepository.findById(id).ifPresent(
                book -> {book.setPerson(null);
                    book.setTakenAt(null);
                });
    }

    @Transactional
    public void giveBook(int idbook, Person selectedPerson) {
        bookRepository.findById(idbook).ifPresent(
                book -> {
                    book.setPerson(selectedPerson);
                    book.setTakenAt(new Date());
                }
        );
    }

    public Person getBookOwner(int id) {
        return bookRepository.findById(id).map(Book::getPerson).orElse(null);
    }

    public List<Book> searchByTitle(String query) {
           return bookRepository.findByNameStartingWith(query);
        }
}
