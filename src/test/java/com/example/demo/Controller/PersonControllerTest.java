package com.example.demo.Controller;

import com.example.demo.Model.Person;
import com.example.demo.Repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    PersonRepository repository; //autowire the crud repo if we need to insert records manually

    @Test
    @Transactional
    @Rollback
    public void testCreate() throws Exception{
        //setup
        //Create a json string
        String json = "{\"firstName\": \"Chloeaa\"}";


        MockHttpServletRequestBuilder request = post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //Execution - send the Person to the request
        this.mvc.perform(request)
                //Assertion
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(1))) //you should not hard codes the id
                .andExpect((jsonPath("$.id", instanceOf(Number.class))))
                .andExpect((jsonPath("$.firstName", is("Chloeaa"))));
    }

    @Test
    @Transactional
    @Rollback
    public void testList() throws Exception{
        //setup
        //Create Person and save it to the database as a test data
        Person person = new Person();
        person.setFirstName("Lou");
        repository.save(person);

        MockHttpServletRequestBuilder request = get("/people");
//                .contentType(MediaType.APPLICATION_JSON); not needed for a Get request since there is no data is required by the end points.

        //Execution - send the Person to the request
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(person.getId().intValue())));
                //instance of number is same as if it exists, it is the same thing
//                .andExpect((jsonPath("$.id").exists()))
//                .andExpect(jsonPath("$[0].name", is("Ammar"))

    }
    @Test
    @Transactional
    @Rollback
    public void getPersonByFirstNameTest() throws Exception{
        //setup
        //Create Person and save it to the database
        //If you have test data, you don't need this
        Person person = new Person();
        person.setFirstName("Ana");
        repository.save(person);

        MockHttpServletRequestBuilder request = get("/people/Ana")
                .contentType(MediaType.APPLICATION_JSON);


        //exectuion
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Ana")));


    }
}

