package com.example.demo.Repository;

import com.example.demo.Model.Lesson;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

//@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {
    Lesson findByTitle(String title);
    Iterable<Lesson> findAllByDeliveredOnBetween(Date from, Date to);
}
