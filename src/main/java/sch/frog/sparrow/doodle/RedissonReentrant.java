package sch.frog.sparrow.doodle;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 验证redisson锁可重入
 */
public class RedissonReentrant {

    private static final Logger logger = LoggerFactory.getLogger(RedissonReentrant.class);

    public static void main(String[] args){
        RedissonClient redissonClient = Redisson.create();
        String lockKey = "TestLock";
        RLock lock1 = redissonClient.getLock(lockKey);
        lock1.lock();
        logger.info("enter to lock1.");

        RLock lock2 = redissonClient.getLock(lockKey);
        // 这句可以执行, 说明可重入
        lock2.lock();
        logger.info("enter to lock2");

        // 这句是可以执行的, 解锁顺序没有要求
        // lock1.unlock();

        lock2.unlock();
        logger.info("lock2 unlock");

        // 虽然lock执行了解锁操作, 但是这里并没有解锁, 输出日志中显示锁定状态为true
        logger.info("lock status for 2 : {}", lock2.isLocked());
        logger.info("lock status for 1 : {}", lock1.isLocked());

        // 这一行可以执行, 也就是说, 可以重复解锁, 只不过总的解锁次数和加锁次数相同即可
        // lock2.unlock();
        // logger.info("repeat unlock");

        lock1.unlock();
        logger.info("lock1 unlock");

        // 完成解锁操作
        logger.info("lock status for 2 : {}", lock2.isLocked());
        logger.info("lock status for 1 : {}", lock1.isLocked());
    }

}
