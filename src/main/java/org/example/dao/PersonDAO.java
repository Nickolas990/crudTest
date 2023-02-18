package org.example.dao;

import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate template;

    @Autowired
    public PersonDAO(JdbcTemplate template) {
        this.template = template;
    }

    public List<Person> index() {
        return template.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return template.query("SELECT * FROM Person where id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findAny()
                .orElse(null);
    }

    public void save(Person person) {
       template.update("INSERT INTO person(name, age, email) VALUES (?,?,?)", person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        template.update("UPDATE person SET name =?, age =?, email =? WHERE id = ?", updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) {
        template.update("DELETE FROM person WHERE id =?", id);
    }

    /////////////////////////////////////////
    /////Batch insert test
    /////////////////////////////////////////



    public void testMultipleInsert() {
        List<Person> people = create1000people();
        long before = System.currentTimeMillis();

        for (Person person : people) {
            template.update("INSERT INTO person(name, age, email) VALUES (?,?,?)", person.getName(), person.getAge(), person.getEmail());
        }
        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    public void testBatchInsert() {
        List<Person> people = create1000people();
        long before = System.currentTimeMillis();
        template.batchUpdate("INSERT INTO person(name, age, email) VALUES (?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, people.get(i).getName());
                preparedStatement.setInt(2, people.get(i).getAge());
                preparedStatement.setString(3, people.get(i).getEmail());
            }
            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));

    }

    private List<Person> create1000people() {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "name" + i, 20 + i, "test" + i + "@test.com"));
        }
        return people;
    }

    public void testBatchUpdate() {
    }
}
