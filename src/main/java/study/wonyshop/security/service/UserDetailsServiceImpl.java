package study.wonyshop.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.wonyshop.common.exception.CustomException;
import study.wonyshop.common.exception.ExceptionStatus;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.repository.UserRepository;

/**
 * 이메일을 통해 유저정보를 담은 UserDetails 을 반환하는 서비스
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = findByUserEmail(email);
    return new UserDetailsImpl(user,user.getEmail());
  }


  private User findByUserEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        ()->new CustomException(ExceptionStatus.WRONG_EMAIL)
    );
  }
  }
