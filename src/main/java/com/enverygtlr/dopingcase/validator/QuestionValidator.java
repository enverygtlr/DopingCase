package com.enverygtlr.dopingcase.validator;

import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuestionValidator implements Validator<QuestionValidator.Context> {

    @Override
    public void validate(Context context) {
        UUID testId = context.testId();
        QuestionRequest request = context.request();

        long correctCount = request.choices().stream()
                .filter(choice -> Boolean.TRUE.equals(choice.isCorrectChoice()))
                .count();

        if (correctCount != 1) {
            throw new ValidationException();
        }
    }

    public record Context(UUID testId, QuestionRequest request) {}
}
