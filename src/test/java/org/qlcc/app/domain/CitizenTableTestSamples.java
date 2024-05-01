package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CitizenTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CitizenTable getCitizenTableSample1() {
        return new CitizenTable()
            .id(1L)
            .citizenID("citizenID1")
            .name("name1")
            .dob("dob1")
            .contact("contact1")
            .gender("gender1")
            .relation("relation1")
            .status("status1");
    }

    public static CitizenTable getCitizenTableSample2() {
        return new CitizenTable()
            .id(2L)
            .citizenID("citizenID2")
            .name("name2")
            .dob("dob2")
            .contact("contact2")
            .gender("gender2")
            .relation("relation2")
            .status("status2");
    }

    public static CitizenTable getCitizenTableRandomSampleGenerator() {
        return new CitizenTable()
            .id(longCount.incrementAndGet())
            .citizenID(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .dob(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .relation(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
