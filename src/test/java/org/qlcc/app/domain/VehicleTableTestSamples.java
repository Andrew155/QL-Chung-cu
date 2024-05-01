package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleTable getVehicleTableSample1() {
        return new VehicleTable()
            .id(1L)
            .vehicleName("vehicleName1")
            .vehicleType("vehicleType1")
            .vehicleId("vehicleId1")
            .roomId("roomId1")
            .ownerId("ownerId1")
            .vehicleFee(1L);
    }

    public static VehicleTable getVehicleTableSample2() {
        return new VehicleTable()
            .id(2L)
            .vehicleName("vehicleName2")
            .vehicleType("vehicleType2")
            .vehicleId("vehicleId2")
            .roomId("roomId2")
            .ownerId("ownerId2")
            .vehicleFee(2L);
    }

    public static VehicleTable getVehicleTableRandomSampleGenerator() {
        return new VehicleTable()
            .id(longCount.incrementAndGet())
            .vehicleName(UUID.randomUUID().toString())
            .vehicleType(UUID.randomUUID().toString())
            .vehicleId(UUID.randomUUID().toString())
            .roomId(UUID.randomUUID().toString())
            .ownerId(UUID.randomUUID().toString())
            .vehicleFee(longCount.incrementAndGet());
    }
}
