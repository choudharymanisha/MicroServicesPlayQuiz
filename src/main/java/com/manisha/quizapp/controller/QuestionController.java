package com.manisha.quizapp.controller;

import com.manisha.quizapp.model.Question;
import com.manisha.quizapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService; // Use the injected instance, not a static reference

    @GetMapping("/allQuestions") // Added leading slash for the URL mapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return  questionService.getAllQuestions();
        // Call method on the instance, not statically

    }
    @GetMapping("category/{category}")
    public  ResponseEntity<List<Question>>getQuestionByCategory(@PathVariable String category){
        return questionService.getQuestionsByCategory(category);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion( @RequestBody Question question){
        return questionService.addQuestion(question);
    }
}
