package org.danilyuk.library.services;

import org.danilyuk.library.models.Book;
import org.danilyuk.library.models.Person;
import org.danilyuk.library.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PersonRepository personRepository;

    @Autowired
    public PeopleService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> index() {
        return personRepository.findAll();
    }

    public Person show(int id) {
        Optional<Person> foundPerson = personRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Person findByName(String name) {
        Optional<Person> foundPerson = personRepository.findByName(name);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonID(int id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMillies > 864000000) book.setExpired(true);
            });
            return person.get().getBooks();
        }
        else {return Collections.emptyList();
        }

    }


}
