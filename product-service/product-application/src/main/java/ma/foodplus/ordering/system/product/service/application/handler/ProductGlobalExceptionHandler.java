package ma.foodplus.ordering.system.product.service.application.handler;

import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.application.handler.ErrorDTO;
import ma.foodplus.ordering.system.application.handler.GlobalExceptionHandler;
import ma.foodplus.ordering.system.product.service.domain.exception.ProductDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ProductGlobalExceptionHandler extends GlobalExceptionHandler{

    @ResponseBody
    @ExceptionHandler(value = {ProductDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ProductDomainException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage()).build();
    }

}
