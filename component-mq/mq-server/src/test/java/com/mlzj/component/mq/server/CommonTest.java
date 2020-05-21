package com.mlzj.component.mq.server;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.utils.ProtostuffUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yhl
 * @date 2020/5/15
 */
public class CommonTest {

    @Test
    public void testSize() {
        User user = new User("1231231231232312312", 123123, User.class);
        User user2 = ProtostuffUtils.deserialize(ProtostuffUtils.serialize(user), User.class);
        System.out.println();
        System.out.println(SerializationUtils.serialize(user).length);
        System.out.println(ProtostuffUtils.serialize(user).length);
    }

    @Test
    public void test(){

        ReentrantLock rt =new ReentrantLock();
        rt.lock();
        new Thread(()->{
            ReentrantLock reentrantLock = new ReentrantLock();
            System.out.println(1);
            reentrantLock.lock();
            System.out.println(2);
        }).start();
    }

}
