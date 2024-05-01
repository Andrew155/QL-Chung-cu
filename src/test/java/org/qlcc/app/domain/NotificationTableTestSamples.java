package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NotificationTable getNotificationTableSample1() {
        return new NotificationTable().id(1).title("title1").content("content1").userID("userID1");
    }

    public static NotificationTable getNotificationTableSample2() {
        return new NotificationTable().id(2).title("title2").content("content2").userID("userID2");
    }

    public static NotificationTable getNotificationTableRandomSampleGenerator() {
        return new NotificationTable()
            .id(intCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .userID(UUID.randomUUID().toString());
    }
}
