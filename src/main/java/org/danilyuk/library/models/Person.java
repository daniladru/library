package org.danilyuk.library.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Neil Alishev
 */
public class Person {
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 150, message = "Name should be between 2 and 150 characters")
    private String name;

    @Min(value = 1900, message = "Birth year should be greater than 1900")
    private int birth_year;

    public Person() {
    }

    public Person(int id, String name, int birth_year) {
        this.id = id;
        this.name = name;
        this.birth_year = birth_year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(int birth_year) {
        this.birth_year = birth_year;
    }
}