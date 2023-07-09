package study.wonyshop.delivery;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  값 타입은 변경 불가능하게 설계해야 한다.  setter 대신 생성자를 통해 값을 초기화함
 */

@Embeddable
@Getter
public class Address {
  private String city;
  private String street;
  private String zipcode;

  public Address(String city, String street, String zipcode) {
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
  }

  public Address() {

  }
}
