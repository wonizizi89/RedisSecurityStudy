package study.wonyshop.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.wonyshop.order.service.OrderService;
import study.wonyshop.security.service.UserDetailsImpl;
import study.wonyshop.user.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  private final OrderService orderService;

//  @DeleteMapping("/soft-delete")
//    public ResponseEntity softDeleteOrder(@AuthenticationPrincipal UserDetailsImpl userDetails ){
//    return orderService.softDeleteOrder(userDetails.getUser().getId(), userDetails.getUser().getRole());
//  }

}
