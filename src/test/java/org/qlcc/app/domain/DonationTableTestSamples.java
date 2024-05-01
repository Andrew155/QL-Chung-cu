package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DonationTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DonationTable getDonationTableSample1() {
        return new DonationTable()
            .id(1L)
            .donationId("donationId1")
            .donationType("donationType1")
            .donationDesc("donationDesc1")
            .donationMonth("donationMonth1")
            .donationCost(1L)
            .roomId("roomId1")
            .status("status1");
    }

    public static DonationTable getDonationTableSample2() {
        return new DonationTable()
            .id(2L)
            .donationId("donationId2")
            .donationType("donationType2")
            .donationDesc("donationDesc2")
            .donationMonth("donationMonth2")
            .donationCost(2L)
            .roomId("roomId2")
            .status("status2");
    }

    public static DonationTable getDonationTableRandomSampleGenerator() {
        return new DonationTable()
            .id(longCount.incrementAndGet())
            .donationId(UUID.randomUUID().toString())
            .donationType(UUID.randomUUID().toString())
            .donationDesc(UUID.randomUUID().toString())
            .donationMonth(UUID.randomUUID().toString())
            .donationCost(longCount.incrementAndGet())
            .roomId(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
