package initiative.guru.spring6plus.spring6reactive.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class CustomValidator {

    private final Validator validator;

    public <T> void validate(T object) {

        BindingResult bindingResult = new BeanPropertyBindingResult(object, object.getClass().getName());
        validator.validate(object, bindingResult);

        if (bindingResult.hasErrors()) {

            Method method = this.getClass().getMethods()[0];
            MethodParameter parameter = new MethodParameter(method, -1);

            throw new WebExchangeBindException(parameter, bindingResult);
        }
    }
}
