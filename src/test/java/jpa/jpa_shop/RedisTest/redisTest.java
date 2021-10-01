package jpa.jpa_shop.RedisTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class redisTest {

    @Autowired
    StringRedisTemplate redisTemplate;
    final String key = "rlaquddus";

    @Test
    public void StringTemplate() {
        // given
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // when
        valueOperations.set(key, "1");
        final String value = valueOperations.get(key);
        log.info(" redis // {} : {}", key, value);
        // then
        valueOperations.increment(key);
        final String value2 = valueOperations.get(key);
        log.info(" redis // {} : {}", key, value2);
        redisTemplate.delete(key);
    }

    @Test
    public void testList() {
        // given
        // when
        final ListOperations<String, String> stringStringListOperations = redisTemplate.opsForList();
        // then
        stringStringListOperations.rightPush(key, "H");
        stringStringListOperations.rightPush(key, "e");
        stringStringListOperations.rightPush(key, "l");
        stringStringListOperations.rightPush(key, "l");
        stringStringListOperations.rightPush(key, "o");

        stringStringListOperations.rightPushAll(key, " ", "k", "b", "y");

        final String index = stringStringListOperations.index(key, 0);
        log.info("select : {}", index);
        final String index2 = stringStringListOperations.index(key, 1);
        log.info("select : {}", index2);
        final List<String> range = stringStringListOperations.range(key,0,7);
        log.info("range : {}", Arrays.toString(range.toArray()));
        redisTemplate.delete(key);
    }
}
