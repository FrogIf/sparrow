package sch.frog.sparrow.doodle;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * 这个例子中, 启动了两个定时任务, 两个定时任务同时去竞争redis锁, 随着不断指定, 最终只剩下一个线程执行, 另一个线程已经终止了
 * 这里有两个因素最终导致了这个结果:
 *   1. ScheduledExecutorService的定时任务执行过程中, 一旦遇到未捕获的异常, 直接终止, 不在继续(并且异常直接被吞掉)
 *   2. redisson释放锁时, 只有持有锁的线程可以释放该锁, 其余线程如果尝试释放, 将直接报错
 */
public class RedissonAndSchedule {

    private static final Logger logger = LoggerFactory.getLogger(RedissonAndSchedule.class);

    private final int mark;

    private final RedissonClient redissonClient;

    public RedissonAndSchedule(int mark, RedissonClient redissonClient){
        this.mark = mark;
        this.redissonClient = redissonClient;
    }

    private void initSchedule(){
        ScheduledExecutorService scheduleExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "ScheduleLockTestThread"));
        scheduleExecutor.scheduleWithFixedDelay(this::scheduleTask, 5, 5, TimeUnit.SECONDS);
    }

    private void scheduleTask(){
        // 这个方法代码块应该整体用try...catch...包起来, 防止定时任务意外终止
        RLock lock = null;
        try {
            lock = redissonClient.getLock("TestLock");
            if(lock.tryLock(5, TimeUnit.SECONDS)){
                logger.info("mark : {} get lock success, do something", mark);
                Thread.sleep(new Random().nextInt(10) + 5000L);
            }else{
                logger.info("mark : {} failed to get lock.", mark);
            }
        } catch (InterruptedException e) {
            logger.error("scheduleTask interrupted exception, msg : {}", e.getMessage(), e);
            Thread.interrupted();
        } catch (Throwable t){
            logger.error("scheduleTask execute exception, msg : {}", t.getMessage(), t);
        } finally{
            if(lock != null){
                // 如果锁不是当前线程持有, 该解锁操作将报错
                lock.unlock();
            }
            // 这段代码才是正确的锁释放逻辑
            // if(lock != null && lock.isLocked() && lock.isHeldByCurrentThread()){
            //     lock.unlock();
            // }
        }
    }

    public static void main(String[] args){
        RedissonClient redissonClient = createRedisson();
        RedissonAndSchedule r1 = new RedissonAndSchedule(1, redissonClient);
        RedissonAndSchedule r2 = new RedissonAndSchedule(2, redissonClient);
        r1.initSchedule();
        r2.initSchedule();
    }

    private static RedissonClient createRedisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

}
