package backend.VocaProject.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionResponseAdvice {

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST) //응답코드: 400 에러로 일단 통일
    public BaseResponse handlerBaseException(BaseException e){
        return new BaseResponse(e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public BaseResponse handlerException(Exception e){
        e.printStackTrace();
        return new BaseResponse(BaseExceptionStatus.SERVER_INTERNAL_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public BaseResponse ValidAnotaionHandler(BindingResult bindingResult){

        List<ObjectError> errors = bindingResult.getAllErrors();
        for(ObjectError error: errors){
            log.info("error.getDefaultMessage() = {} ", error.getDefaultMessage());
        }

        String errorReason = errors.get(0).getDefaultMessage();
        return new BaseResponse(2001, errorReason);
    }
}
