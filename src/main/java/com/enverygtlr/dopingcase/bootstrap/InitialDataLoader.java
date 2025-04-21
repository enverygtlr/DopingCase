package com.enverygtlr.dopingcase.bootstrap;

import com.enverygtlr.dopingcase.domain.entity.Student;
import com.enverygtlr.dopingcase.domain.request.ChoiceRequest;
import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.domain.request.TestRequest;
import com.enverygtlr.dopingcase.repository.StudentRepository;
import com.enverygtlr.dopingcase.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class InitialDataLoader {

    private final StudentRepository studentRepository;
    private final TestService testService;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            studentRepository.saveAll(List.of(
                    Student.builder()
                            .firstName("Ada")
                            .lastName("Lovelace")
                            .email("ada@example.com")
                            .studentNo(1001L)
                            .build(),
                    Student.builder()
                            .firstName("Alan")
                            .lastName("Turing")
                            .email("alan@example.com")
                            .studentNo(1002L)
                            .build()
            ));

            createTestWithQuestions("Java Basics Test", "Java Q");
            createTestWithQuestions("Algorithms Test", "Algo Q");
        };
    }

    private void createTestWithQuestions(String testTitle, String questionPrefix) {
        List<QuestionRequest> questions = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            List<ChoiceRequest> choices = new ArrayList<>();
            for (int j = 1; j <= 4; j++) {
                boolean isCorrect = j == 2;
                choices.add(new ChoiceRequest("Choice " + j, isCorrect));
            }
            questions.add(new QuestionRequest(questionPrefix + " " + i, choices));
        }

        TestRequest request = new TestRequest(testTitle, questions);
        testService.createTest(request);
    }
}