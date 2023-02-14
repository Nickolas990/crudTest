package org.example.dao;

import org.example.models.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private List<Person> people;
    private int PEOPLE_COUNT = 0;


    {
        people = new ArrayList<>();
        people.add(new Person(PEOPLE_COUNT++, "John"));
        people.add(new Person(PEOPLE_COUNT++, "Jane"));
        people.add(new Person(PEOPLE_COUNT++, "James"));
        people.add(new Person(PEOPLE_COUNT++, "Michael"));
        people.add(new Person(PEOPLE_COUNT++, "Mary"));
        people.add(new Person(PEOPLE_COUNT++, "Mike"));
        people.add(new Person(PEOPLE_COUNT++, "Lesley"));
        people.add(new Person(PEOPLE_COUNT++, "Joanna"));
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int id) {
        return people.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void save(Person person) {
        person.setId(PEOPLE_COUNT++);
        people.add(person);
    }
}
