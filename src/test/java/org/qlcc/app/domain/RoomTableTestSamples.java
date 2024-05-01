package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoomTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RoomTable getRoomTableSample1() {
        return new RoomTable()
            .id(1L)
            .roomId("roomId1")
            .area("area1")
            .ownTime("ownTime1")
            .ownerId("ownerId1")
            .ownerName("ownerName1")
            .status("status1");
    }

    public static RoomTable getRoomTableSample2() {
        return new RoomTable()
            .id(2L)
            .roomId("roomId2")
            .area("area2")
            .ownTime("ownTime2")
            .ownerId("ownerId2")
            .ownerName("ownerName2")
            .status("status2");
    }

    public static RoomTable getRoomTableRandomSampleGenerator() {
        return new RoomTable()
            .id(longCount.incrementAndGet())
            .roomId(UUID.randomUUID().toString())
            .area(UUID.randomUUID().toString())
            .ownTime(UUID.randomUUID().toString())
            .ownerId(UUID.randomUUID().toString())
            .ownerName(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
