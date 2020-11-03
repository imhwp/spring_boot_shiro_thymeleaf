package im.hwp.shiro.cache;

import im.hwp.utils.ApplicationContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class RedisCache<k,v> implements Cache<k,v> {
    private String cacheName;

    public RedisCache(String cacheName) {
        this.cacheName = cacheName;
    }
    public RedisCache() {
    }

    @Override
    public v get(k k) throws CacheException {
        RedisTemplate redisTemplate = getRedistemplate();
        return (v) redisTemplate.opsForHash().get(this.cacheName, k.toString());
    }

    @Override
    public v put(k k, v v) throws CacheException {
        System.out.println("put key:" + k);
        System.out.println("put value:" + v);
        RedisTemplate redisTemplate = getRedistemplate();
        redisTemplate.opsForHash().put(this.cacheName, k.toString(), v);
        return null;

    }

    @Override
    public v remove(k k) throws CacheException {
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<k> keys() {
        return null;
    }

    @Override
    public Collection<v> values() {
        return null;
    }

    private RedisTemplate getRedistemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
