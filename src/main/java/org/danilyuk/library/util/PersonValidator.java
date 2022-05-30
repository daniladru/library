package org.danilyuk.library.util;

import org.danilyuk.library.models.Person;
import org.danilyuk.library.repositories.PersonRepository;
import org.danilyuk.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Person foundPerson = peopleService.findByName(person.getName());

        if (foundPerson != null) {
            errors.rejectValue("name","","This name is already taken");
        }
    }
}