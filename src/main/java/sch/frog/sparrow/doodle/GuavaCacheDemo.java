package sch.frog.sparrow.doodle;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Guava cache例程
 */
public class GuavaCacheDemo {

    private static final Cache<String, String> cache = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(2000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private static final LoadingCache<String, String> loadCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(2000)
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .refreshAfterWrite(3, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    System.out.println("load");
                    // 假装这里是一个查询数据库
                    return String.valueOf(s.hashCode() + "-" + new Date().getTime());
                }

                @Override
                public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                    try{
                        System.out.println("reload");
                        return super.reload(key, oldValue);
                    }catch (Exception e){
                        return Futures.immediateFuture(oldValue);
                    }
                }
            });

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        cache.put("aaa", "bbb");
        System.out.println(cache.getIfPresent("aaa"));
        System.out.println(cache.getIfPresent("ccc"));

        for(int i = 0; i < 100; i++){
            System.out.println(loadCache.get("vvv"));
            Thread.sleep(5000);
        }
    }

}
