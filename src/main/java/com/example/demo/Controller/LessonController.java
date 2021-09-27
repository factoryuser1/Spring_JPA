package com.example.demo.Controller;


import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    //inject the lesson repository using constructor injection
    private final LessonRepository lessonTable;

    public LessonController(LessonRepository lessonTable) {
        this.lessonTable = lessonTable;
    }

    public static Date getDateFromString(String dateAsString) {
        String[] dateValues = dateAsString.split("-");
        return new Date(Integer.valueOf(dateValues[0]) - 1900, Integer.valueOf(dateValues[1]) - 1, Integer.valueOf(dateValues[2]) - 0);
    }

    @GetMapping("")
    public Iterable<Lesson> finAll() {
        return this.lessonTable.findAll();
    }

    @PostMapping("")
    public Lesson createLesson(@RequestBody Lesson lesson) {
        return this.lessonTable.save(lesson);
    }

    @PatchMapping("/{id}")
    public Lesson updateLesson(@PathVariable Long id, @RequestBody HashMap<String, String> lessonMap) throws ParseException {
        Lesson lessonReturn = lessonTable.findById(id).get();

        if (lessonMap.containsKey("title")) {
            lessonReturn.setTitle(lessonMap.get("title"));
        }
        if (lessonMap.containsKey("deliveredOn")) {
            Date deliveredOn = getDateFromString(lessonMap.get("deliveredOn"));
            lessonReturn.setDeliveredOn(deliveredOn);
        }
        return lessonTable.save(lessonReturn);
    }

    @GetMapping("/find/{title}")
    public Lesson findLessonByTitle(@PathVariable String title) {
        return lessonTable.findByTitle(title);
    }

    @GetMapping("/{id}")
    public Optional<Lesson> getLessonById(@PathVariable Long id) {
        Optional<Lesson> lesson = lessonTable.findById(id);
        return lesson;
    }

    @DeleteMapping("/{id}")
    public String deleteLessonById(@PathVariable Long id) {
        try {
            lessonTable.deleteById(id);
            return "Lesson " + id + " was deleted successfully!";
        } catch (Exception e) {
            return "Unable to delete lesson #: " + id;
        }
    }

    @GetMapping("/between")
    public Iterable<Lesson> getLessonsThatOccurBetweenDates(@RequestParam(value = "date1") String from, @RequestParam(value = "date2") String to) {
        Date fromDate = getDateFromString(from);
        Date toDate = getDateFromString(to);
        var queryResults = lessonTable.findAllByDeliveredOnBetween(fromDate, toDate);
        return queryResults;
    }

}
