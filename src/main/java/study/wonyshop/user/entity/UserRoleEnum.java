package study.wonyshop.user.entity;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
  ADMIN(Authority.ADMIN),
  MEMBER(Authority.MEMBER),
  SELLER(Authority.SELLER);
  
  
  private final String authority;

  UserRoleEnum(String authority) {
    this.authority = authority;
  }
  
  public static class Authority{
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String SELLER = "ROLE_SELLER";
    public static final String MEMBER = "ROLE_MEMBER";
    
  }
}
