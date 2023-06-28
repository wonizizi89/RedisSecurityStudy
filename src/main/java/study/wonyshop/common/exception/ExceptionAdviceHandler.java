package study.wonyshop.common.exception;


import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviceHandler {


  @ExceptionHandler({CustomException.class})
  //   @ResponseStatus(HttpStatus.BAD_REQUEST)  정적으로 예외에 대한 값 설정
  protected ResponseEntity handleApiException(CustomException customException) {
    return new ResponseEntity<>(new ErrorDto(
        customException.getExceptionStatus().getStatusCode(),
        customException.getExceptionStatus().getMessage()),
        HttpStatus.valueOf(customException.getExceptionStatus()
            .getStatusCode())); //예외처리의 상태코드를 가진 HttpStatus 객체를 반환, 동적으로 예외값 처리
    //HttpStatus.valueOf(customException.getExceptionStatus().getStatusCode()) 을 간략히   HttpStatus.BAD_REQUEST 대신 사용 가능
  }

  //상태코드를 404 로 넣어줌
  @ResponseStatus(HttpStatus.NOT_FOUND) // 별도로 ResponseEntity를 생성할 필요 없이 예외가 발생할 때 자동으로 NOT_FOUND 응답이 반환됩니다.
  @ExceptionHandler(IllegalArgumentException.class)
  protected ErrorDto handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn(e.getMessage());
    return new ErrorDto(404, e.getMessage());
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler({SecurityException.class})
  protected ErrorDto SecurityExceptionHandler(SecurityException e){
    log.warn(e.getMessage());
    return new ErrorDto(403,e.getMessage());
  }

  @ExceptionHandler({NullPointerException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity handleNullPointerException(NullPointerException e){
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({NoSuchElementException.class})
  protected ErrorDto handleMethodNotFindException(NoSuchElementException e){
    log.warn(e.getMessage());
    return new ErrorDto(403,e.getMessage());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  protected ResponseEntity handleMethodNotValidException(MethodArgumentNotValidException e){
    log.warn(e.getMessage());
    return new ResponseEntity<>(e.getBindingResult().getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
    //e.getBindingResult().getFieldError().getDefaultMessage()를 통해 유효성 검사에서 실패한 필드의 기본 메시지를 가져옵니다.
  }

  @ExceptionHandler({RuntimeException.class})
  protected ResponseEntity handleEtcException(RuntimeException e){
    return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST );
  }
}


