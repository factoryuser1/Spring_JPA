package com.example.demo.Controller;

import com.example.demo.Model.Person;
import com.example.demo.Repository.PersonRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {
    //inject the Person Repository using constructor injection
    private final PersonRepository personTable;

    public PersonController(PersonRepository personTable) {
        this.personTable = personTable;
    }

    @PostMapping("/people") //create person
    public Person createPerson(@RequestBody Person person) {
        return this.personTable.save(person);
    }

    @GetMapping("/people") //retrieve persons list
    public Iterable<Person> getPersonsList(){
        return this.personTable.findAll();
    }
    @GetMapping("/people/{firstName}")
    public Iterable<Person> getPersonByFirstName(@PathVariable(name = "firstName" ) String name){
       return this.personTable.findByFirstName(name);
    }

}
