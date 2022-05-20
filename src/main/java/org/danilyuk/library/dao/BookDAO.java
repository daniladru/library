package org.danilyuk.library.dao;

import org.danilyuk.library.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author Neil Alishev
 */
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
      return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }



    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(name,author,year,idperson) VALUES(?,?,?,0)", book.getName(), book.getAuthor(),book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET name=?, author=?, year = ?, idperson=? where id = ?", updatedBook.getName(),updatedBook.getAuthor(),updatedBook.getYear() ,updatedBook.getIdPerson(),id);
    }

    public void freeBook(int id) {
        jdbcTemplate.update("UPDATE Book SET idperson=0 where id = ?",id);
    }

    public void giveBook(int idbook,int idperson) {
        jdbcTemplate.update("UPDATE Book SET idperson=? where id = ?",idperson, idbook);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }
}