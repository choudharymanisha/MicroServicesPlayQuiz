package com.manisha.quizapp.service;

import com.manisha.quizapp.dao.QuestionDao;
import com.manisha.quizapp.dao.QuizDao;
import com.manisha.quizapp.model.Question;
import com.manisha.quizapp.model.QuestionWrapper;
import com.manisha.quizapp.model.Quiz;
import com.manisha.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quizOptional = quizDao.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            List<Question> questionsFromDB = quiz.getQuestions();
            List<QuestionWrapper> questionsForUser = new ArrayList<>();
            for (Question q : questionsFromDB) {
                QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
                questionsForUser.add(qw);
            }
            return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
        } else {
            // Handle case where quiz with given ID does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Integer> calculatedResult(Integer id, List<Response> responses) {
        Optional<Quiz> quizOptional = quizDao.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            List<Question> questions = quiz.getQuestions();
            int correctAnswers = 0;
            int i = 0;
            for (Response response : responses) {
                if (response.getResponse().equals(questions.get(i).getRightAnswer())) {
                    correctAnswers++;
                }
                i++;
            }
            return new ResponseEntity<>(correctAnswers, HttpStatus.OK);
        } else {
            // Handle case where quiz with given ID does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
