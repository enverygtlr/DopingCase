package com.enverygtlr.dopingcase.validator;

import com.enverygtlr.dopingcase.domain.request.ChoiceRequest;
import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.exception.ValidationException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class QuestionValidatorTest {

    private final QuestionValidator validator = new QuestionValidator();

    static Stream<Arguments> provideChoiceScenarios() {
        return Stream.of(
                Arguments.of("no correct choices", List.of(
                        new ChoiceRequest("A", false),
                        new ChoiceRequest("B", false)
                ), true),

                Arguments.of("multiple correct choices", List.of(
                        new ChoiceRequest("A", true),
                        new ChoiceRequest("B", true)
                ), true),

                Arguments.of("exactly one correct choice", List.of(
                        new ChoiceRequest("A", true),
                        new ChoiceRequest("B", false)
                ), false),

                Arguments.of("single correct choice only", List.of(
                        new ChoiceRequest("Only one", true)
                ), true)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideChoiceScenarios")
    void validate_shouldValidateCorrectChoiceCount(String description, List<ChoiceRequest> choices, boolean shouldThrow) {
        // Given
        QuestionRequest request = new QuestionRequest("Some question", choices);
        QuestionValidator.Context context = new QuestionValidator.Context(request);

        // When - Then
        if (shouldThrow) {
            assertThrows(ValidationException.class, () -> validator.validate(context));
        } else {
            assertDoesNotThrow(() -> validator.validate(context));
        }
    }
}
