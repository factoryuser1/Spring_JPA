package com.example.demo.Controller;

import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.example.demo.Controller.LessonController.getDateFromString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerTest {
    @Autowired
    LessonRepository repository;
    @Autowired
    private MockMvc mvc;

    private Lesson populateDBWithOneEntry(String title, String dateAsString) {
        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        Date deliveredOn = getDateFromString(dateAsString);
        lesson.setDeliveredOn(deliveredOn);
        repository.save(lesson);
        return lesson;
    }

    @Test
    @Transactional
    @Rollback
    public void getLessonsListTest() throws Exception {
        //Setup
        Lesson lesson = populateDBWithOneEntry("Jason Borne", "2021-9-27");

        RequestBuilder request = get("/lessons");

        //execute Option+Command+V to extract a variable for a Type
        ResultActions perform = this.mvc.perform(request);

        //assert
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(lesson.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is("Jason Borne")))
                .andExpect(jsonPath("$[0].deliveredOn", is("2021-09-27")));

    }

    @Test
    @Transactional
    @Rollback
    public void getLessonByIdTest() throws Exception {
        //Setup
        Lesson lesson = populateDBWithOneEntry("Jason Borne", "2021-9-27");

//        RequestBuilder request = get("/lessons/4");
        RequestBuilder request = get("/lessons/%d".formatted(lesson.getId()));

        //execute Option+Command+V to extract a variable for a Type
        ResultActions perform = this.mvc.perform(request);

        //assert
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(lesson.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Jason Borne")))
                .andExpect(jsonPath("$.deliveredOn", is("2021-09-27")));

    }

    @Test
    @Transactional
    @Rollback
    public void shouldSaveNewLessonInDB() throws Exception {
        var testJSON = """
                {
                    "title": "Intro To DB",
                    "deliveredOn": "2021-09-27"
                }
                """;

//        var testJSON = """
//                [{
//                    "title": "Intro To DB",
//                    "deliveredOn": "2021-09-27"
//                },
//                {
//                    "title": "Intro To DB",
//                    "deliveredOn": "2021-09-27"
//                }]
//                """;

        RequestBuilder request = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testJSON)
                .accept(MediaType.APPLICATION_JSON);

        var response = mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Intro To DB")))
                .andExpect(jsonPath("$.deliveredOn", is("2021-09-27")));
    }

    @Test
    @Transactional
    @Rollback
    public void ShouldPatchLessonRequestById() throws Exception {
        var testJSON = """
                {
                      "title": "Spring Security",
                      "deliveredOn": "2017-04-12"
                }
                """;

        Lesson lesson = populateDBWithOneEntry("Jason Borne", "2021-9-27");

        var testJSONResult = """
                {
                       "id": %d,
                        "title": "Spring Security",
                        "deliveredOn": "2017-04-12"
                }
                """.formatted(lesson.getId());

        RequestBuilder request = patch("/lessons/%d".formatted(lesson.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(testJSON);

        var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(content().json(testJSONResult));

    }

    @Test
    @Transactional
    @Rollback
    public void ShouldFindLessonByTitle() throws Exception {
        Lesson lesson = populateDBWithOneEntry("SQL", "2021-9-27");
        RequestBuilder request = get("/lessons/find/%s".formatted(lesson.getTitle()));
        var testJSONResult = """
                {
                       "id": %d,
                        "title": "SQL",
                        "deliveredOn": "2021-09-27"
                }
                """.formatted(lesson.getId());

        //execute Option+Command+V to extract a variable for a Type
        ResultActions perform = this.mvc.perform(request);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(testJSONResult));
    }

    @Test
    @Transactional
    @Rollback
    public void shouldReturnLessonsThatOccurBetweenDatesInput() throws Exception {
        Lesson lesson1 = populateDBWithOneEntry("SQL", "2021-9-27");
        Lesson lesson2 = populateDBWithOneEntry("Chemistry", "2015-9-27");
        Lesson lesson3 = populateDBWithOneEntry("Biology", "2010-9-27");

        var request = get("/lessons/between?date1=2009-10-10&date2=2016-02-13");

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
