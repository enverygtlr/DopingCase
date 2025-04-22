package com.enverygtlr.dopingcase.validator;

import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionValidator implements Validator<QuestionValidator.Context> {

    @Override
    public void validate(Context context) {
        QuestionRequest request = context.request();

        long correctCount = request.choices().stream()
                .filter(choice -> Boolean.TRUE.equals(choice.isCorrectChoice()))
                .count();

        if (correctCount != 1 || request.choices().size() < 2) {
            throw ValidationException.questionIsNotValid();
        }
    }

    public record Context(QuestionRequest request) {}
}
