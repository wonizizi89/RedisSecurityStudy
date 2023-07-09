package study.wonyshop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect //여러객체에 공통으로 적용되는 기능을 제공하는 클리스 지정
@Component
public class ExecutionTimer {

  @Pointcut("@annotation(study.wonyshop.aop.Timer)") //Joinpoint의 부분 집합, 실제 Advice가 적용되는 Joinpoint를 나타냄
  private void timer(){}

    // 패키지의 Controller 라는 이름으로 끝나는 패키지에 모두 적용 (매서드만)
    @Pointcut("within(*..*Controller)")
    private void cut(){}
    // 메서드 전 후로 시간 측정 시작하고 멈추려면 Before,
    // AfterReturning으로는 시간을 공유 할 수가 없음 Around 사용!


//⭐️ Around Advice: 대상 메서드의 실행 전과 후 모두 실행됩니다. 대상 메서드를 직접 호출하는 것과 비슷하게 작동하며,
// 대상 메서드를 실행하는 ProceedingJoinPoint 객체를 파라미터로 전달받아 직접 실행 결정을 할 수 있다.
// Advice가 대상 메서드의 실행을 제어할 수 있다.



  //proceedingJoinPoint 메서드?
  //Advice가 적용될 지점은 Join Point(조인 포인트)라고 부르며, Join Point의 실행을 계속 진행하기 위한 메서드를 "proceedingJointPoint"라고 합니다.
  //즉 Advice가 적용된 메서드의 실행을 계속해서 진행하기 위한 지점을 가리키는 용어이다.
    @Around("timer()")// timer() 포인트컷을 사용하여 해당 어노테이션이 적용된 메서드의 실행 전후에 코드를 수행하도록 합니다.
   public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed();
        // 매서드를 실행시, 프록시 객체가 아닌 실제 대상 객체의 메서드 호출 한다.
         // 그래서 이 코드 전후로 공통기능을 위한 코드를 위치 시켜야한다.

        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 현재 실행 중인 조인 포인트의 시그니처(메서드의 정보)를 가져옵니다.
      // joinPoint 객체는 현재 실행 중인 메서드에 대한 정보를 제공하는 객체입니다.


      String methodName = signature.getMethod().getName();
      //getMethod().getName()은 시그니처를 통해 메서드의 이름을 추출하는 메서드입니다.
      // signature.getMethod()을 호출하여 메서드 객체를 가져온 다음 getName()을 호출하여 메서드의 이름을 문자열로 얻을 수 있습니다.

        log.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        return proceed;
    }
}

