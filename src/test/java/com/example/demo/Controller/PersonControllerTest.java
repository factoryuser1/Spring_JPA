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
    PersonRepository repository;

    @Test
    @Transactional
    @Rollback
    public void testCreate() throws Exception{
        MockHttpServletRequestBuilder request = post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"Chloeaa\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", instanceOf(Number.class))));
    }

    @Test
    @Transactional
    @Rollback
    public void testList() throws Exception{

        Person person = new Person();
        person.setFirstName("Lou");
        repository.save(person);

        MockHttpServletRequestBuilder request = get("/people")
                .contentType(MediaType.APPLICATION_JSON);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(person.getId().intValue())));
    }
}
