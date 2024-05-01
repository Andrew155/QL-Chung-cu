package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApplicationUser getApplicationUserSample1() {
        return new ApplicationUser()
            .id(1L)
            .familyId("familyId1")
            .citizenID("citizenID1")
            .name("name1")
            .dob("dob1")
            .contact("contact1")
            .gender("gender1")
            .relation("relation1")
            .status("status1")
            .roomId("roomId1");
    }

    public static ApplicationUser getApplicationUserSample2() {
        return new ApplicationUser()
            .id(2L)
            .familyId("familyId2")
            .citizenID("citizenID2")
            .name("name2")
            .dob("dob2")
            .contact("contact2")
            .gender("gender2")
            .relation("relation2")
            .status("status2")
            .roomId("roomId2");
    }

    public static ApplicationUser getApplicationUserRandomSampleGenerator() {
        return new ApplicationUser()
            .id(longCount.incrementAndGet())
            .familyId(UUID.randomUUID().toString())
            .citizenID(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .dob(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .relation(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .roomId(UUID.randomUUID().toString());
    }
}
