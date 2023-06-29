package study.wonyshop.security.service;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.entity.UserRoleEnum;
@Getter
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private User user;
  private String username;

  public UserDetailsImpl(User user, String username) {
    this.user = user;
    this.username = username;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    UserRoleEnum role = user.getRole();
    String authority = role.getAuthority();
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
    Collection<GrantedAuthority>authorities = new ArrayList<>();
    authorities.add(simpleGrantedAuthority); // 권한을 simpleGrantedAuthority로 추상화하여 관리함
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
