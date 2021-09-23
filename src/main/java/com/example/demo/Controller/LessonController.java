package com.example.demo.Controller;


import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    //inject the lesson repository using constructor injection
    private final LessonRepository lessonTable;

    public LessonController(LessonRepository lessonTable){
        this.lessonTable = lessonTable;
    }

    @GetMapping("")
    public Iterable<Lesson> finAll() {
        return this.lessonTable.findAll();
    }

    @PostMapping("")
    public Lesson createLesson(@RequestBody Lesson lesson){
        return this.lessonTable.save(lesson);
    }

    @GetMapping("/{id}")
    public Optional<Lesson> getLessonById(@PathVariable Long id){
        Optional<Lesson> lesson = lessonTable.findById(id);

        return lesson;
    }
    @DeleteMapping("/{id}")
    public String deleteLessonById(@PathVariable Long id){
        try{
            lessonTable.deleteById(id);
            return "Lesson " + id + " was deleted successfully!";
        } catch (Exception e){
            return "Unable to delete lesson #: " + id;
        }
    }
}
