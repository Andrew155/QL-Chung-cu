package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BillTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BillTable getBillTableSample1() {
        return new BillTable()
            .id(1L)
            .billType("billType1")
            .billId("billId1")
            .billMonth("billMonth1")
            .billAmount(1)
            .roomId("roomId1")
            .status("status1")
            .billCost(1L)
            .customerID("customerID1");
    }

    public static BillTable getBillTableSample2() {
        return new BillTable()
            .id(2L)
            .billType("billType2")
            .billId("billId2")
            .billMonth("billMonth2")
            .billAmount(2)
            .roomId("roomId2")
            .status("status2")
            .billCost(2L)
            .customerID("customerID2");
    }

    public static BillTable getBillTableRandomSampleGenerator() {
        return new BillTable()
            .id(longCount.incrementAndGet())
            .billType(UUID.randomUUID().toString())
            .billId(UUID.randomUUID().toString())
            .billMonth(UUID.randomUUID().toString())
            .billAmount(intCount.incrementAndGet())
            .roomId(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .billCost(longCount.incrementAndGet())
            .customerID(UUID.randomUUID().toString());
    }
}
