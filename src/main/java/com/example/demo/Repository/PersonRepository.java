package com.example.demo.Repository;

import com.example.demo.Model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    //create a query for me that searches the Person table by firstname
    //writing a method signature to create a query for us in the db
    //
    Iterable<Person> findByFirstName(String firstName);
}
