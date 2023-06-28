package study.wonyshop.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.wonyshop.redis.RedisDao;
import study.wonyshop.security.exception.CustomAccessDeniedHandler;
import study.wonyshop.security.exception.CustomAuthenticationEntryPoint;
import study.wonyshop.security.jwt.JwtAuthFilter;
import study.wonyshop.security.jwt.JwtProvider;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
//@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
@EnableMethodSecurity // 위 어노테이션은 Deprecated
@EnableScheduling // @Scheduled 어노테이션 활성화
public class WebSecurityConfig implements WebMvcConfigurer {
  private final JwtProvider jwtProvider;
  private final RedisDao redisDao;

  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // h2-console 사용 및 resources 접근 허용 설정
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  /**
   * 필터를 타고 세션이 아닌 jwt방식을 타고 권한을
   * 확인하는 메소드
   * 스프링부트 3,0 미만은 .antMatchers() 로,
   *          3.0 이상은 .requestMatchers() 로 해야 오류가 발생하지 않음
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();
    // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeHttpRequests()
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .antMatchers("/api/users/signup").permitAll()
        .antMatchers("/api/users/login").permitAll()
//        .antMatchers("/owner/**").hasAnyRole("Owner", "Manager", "GeneralManager")
//              .requestMatchers("/api/general/**").hasRole("GeneralManager")
        .anyRequest().authenticated()//인증이 되어야 한다는 이야기이다.
        .and()
        // JWT 인증/인가를 사용하기 위한 설정
        .addFilterBefore(new JwtAuthFilter(redisDao, jwtProvider),
            UsernamePasswordAuthenticationFilter.class);
    // xss 공격을 막기위한 필터 설정
//            .addFilterBefore(new XssEscapeFilter(xssEscapeUtil), CsrfFilter.class)

    // 401 Error 처리, Authorization 즉, 인증과정에서 실패할 시 처리
    http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
    // 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우
    http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
    return http.build();
  }
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
        .allowedOriginPatterns("*")
        .exposedHeaders("Authorization");
  }
  }

