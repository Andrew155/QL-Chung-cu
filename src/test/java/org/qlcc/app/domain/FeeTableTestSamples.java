package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeeTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeeTable getFeeTableSample1() {
        return new FeeTable()
            .id(1L)
            .feeType("feeType1")
            .feeDesc("feeDesc1")
            .feeMonth("feeMonth1")
            .feeCost(1L)
            .status("status1")
            .feeId("feeId1");
    }

    public static FeeTable getFeeTableSample2() {
        return new FeeTable()
            .id(2L)
            .feeType("feeType2")
            .feeDesc("feeDesc2")
            .feeMonth("feeMonth2")
            .feeCost(2L)
            .status("status2")
            .feeId("feeId2");
    }

    public static FeeTable getFeeTableRandomSampleGenerator() {
        return new FeeTable()
            .id(longCount.incrementAndGet())
            .feeType(UUID.randomUUID().toString())
            .feeDesc(UUID.randomUUID().toString())
            .feeMonth(UUID.randomUUID().toString())
            .feeCost(longCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .feeId(UUID.randomUUID().toString());
    }
}
