package study.wonyshop.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 레디스 데이터베이스 이용
 * redisTemplate 를 이용하여 레디스의 데이터를 저장,조회, 삭제함
 */
@Component
@RequiredArgsConstructor
public class
RedisDao {

  private final RedisTemplate<String, String> redisTemplate;

  public void setRefreshToken(String email, String refreshToken, Long minutes) {
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));
    redisTemplate.opsForValue().set(email, refreshToken, minutes, TimeUnit.MINUTES);
  }

  public String getRefreshToken(String key) {
    return  redisTemplate.opsForValue().get(key);
  }

  public void deleteRefreshToken(String key) {
    redisTemplate.delete(key);
  }

  public boolean hasKey(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public void setBlackList(String accessToken, String msg, Long minutes) {
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(msg.getClass()));
    redisTemplate.opsForValue().set(accessToken, msg, minutes, TimeUnit.MINUTES);
  }

  public String getBlackList(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public boolean deleteBlackList(String key) {
    return Boolean.TRUE.equals(redisTemplate.delete(key));
  }

  public boolean hasKeyBlackList(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }
}