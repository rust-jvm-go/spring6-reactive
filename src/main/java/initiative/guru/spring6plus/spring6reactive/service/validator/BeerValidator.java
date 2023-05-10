package initiative.guru.spring6plus.spring6reactive.service.validator;

import initiative.guru.spring6plus.spring6reactive.domain.model.Beer;
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
public class BeerValidator {

    private final Validator validator;

    public void validate(Beer beer) {

        Errors errors = new BeanPropertyBindingResult(beer, "beer");
        validator.validate(beer, errors);

        if (errors.hasErrors()) {

            String errorDetails = errors.getAllErrors().stream()
                .map(ObjectError::toString)
                .collect(Collectors.joining(", "));

            throw new ServerWebInputException(errorDetails);
        }
    }
}
