package com.example.demo.Repository;

import com.example.demo.Model.Lesson;
import org.springframework.data.repository.CrudRepository;

//@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {
}
