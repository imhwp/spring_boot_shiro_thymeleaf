package im.hwp.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        System.out.println("RedisCacheManager.getCache被调用:" + s);
        return new RedisCache<K,V>(s);
    }
}
