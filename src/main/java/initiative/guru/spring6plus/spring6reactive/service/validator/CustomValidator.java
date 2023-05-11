package initiative.guru.spring6plus.spring6reactive.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomValidator {

    private final Validator validator;

    public <T> void validate(T object) {

        Errors errors = new BeanPropertyBindingResult(object, object.getClass().getName());
        validator.validate(object, errors);

        if (errors.hasErrors()) {

            String errorDetails = errors.getAllErrors().stream()
                .map(ObjectError::toString)
                .collect(Collectors.joining(", "));

            throw new ServerWebInputException(errorDetails);
        }
    }
}
