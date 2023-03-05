package extend;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/19 21:09
 */
@Slf4j
public class ThreadTest {


    @Test
    public void testParkInterrupt() {

        Thread thread1 = new Thread(() -> {
            log.info("你好，我是线程1，没人打扰的话，将在10秒后醒过来");
            long nanos = TimeUnit.SECONDS.toNanos(10);
            LockSupport.parkNanos(Thread.currentThread(), nanos);
            log.info("被人叫醒了");
        });


        Thread thread2 = new Thread(() -> {

            log.info("你好，我是线程2，2秒后准备唤醒线程1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
            }
            thread1.interrupt();
            log.info("叫醒了-{}", thread1.isInterrupted());
        });

        thread1.start();
        thread2.start();

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (Exception e) {

        }

    }
}
