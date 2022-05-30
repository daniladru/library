package org.danilyuk.library.repositories;

import org.danilyuk.library.models.Book;
import org.danilyuk.library.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByNameStartingWith(String title);
}
