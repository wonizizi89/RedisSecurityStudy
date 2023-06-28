package study.wonyshop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
private final ExceptionStatus exceptionStatus;
}
