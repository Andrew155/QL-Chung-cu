package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.RoomTableTestSamples.*;
import static org.qlcc.app.domain.VehicleTableTestSamples.*;

import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class VehicleTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleTable.class);
        VehicleTable vehicleTable1 = getVehicleTableSample1();
        VehicleTable vehicleTable2 = new VehicleTable();
        assertThat(vehicleTable1).isNotEqualTo(vehicleTable2);

        vehicleTable2.setId(vehicleTable1.getId());
        assertThat(vehicleTable1).isEqualTo(vehicleTable2);

        vehicleTable2 = getVehicleTableSample2();
        assertThat(vehicleTable1).isNotEqualTo(vehicleTable2);
    }

    @Test
    void roomTableTest() throws Exception {
        VehicleTable vehicleTable = getVehicleTableRandomSampleGenerator();
        RoomTable roomTableBack = getRoomTableRandomSampleGenerator();

        vehicleTable.setRoomTable(roomTableBack);
        assertThat(vehicleTable.getRoomTable()).isEqualTo(roomTableBack);

        vehicleTable.roomTable(null);
        assertThat(vehicleTable.getRoomTable()).isNull();
    }
}
