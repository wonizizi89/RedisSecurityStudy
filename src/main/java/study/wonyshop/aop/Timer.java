package study.wonyshop.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE} ) // 다른 메서드나 어노테이에 적용할수 있도록 대상을 지정하는 어노테이션
@Retention(RetentionPolicy.RUNTIME) //어노테이션의 정보 유지 범위를 지정 어노테이션으로 런타임 시점까지 유지한다라는 뜻
public @interface Timer { //Timer 라는 어노테이션 정의

}
