package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.BillTableTestSamples.*;
import static org.qlcc.app.domain.CitizenTableTestSamples.*;
import static org.qlcc.app.domain.DonationTableTestSamples.*;
import static org.qlcc.app.domain.FeeTableTestSamples.*;
import static org.qlcc.app.domain.RoomTableTestSamples.*;
import static org.qlcc.app.domain.VehicleTableTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class RoomTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomTable.class);
        RoomTable roomTable1 = getRoomTableSample1();
        RoomTable roomTable2 = new RoomTable();
        assertThat(roomTable1).isNotEqualTo(roomTable2);

        roomTable2.setId(roomTable1.getId());
        assertThat(roomTable1).isEqualTo(roomTable2);

        roomTable2 = getRoomTableSample2();
        assertThat(roomTable1).isNotEqualTo(roomTable2);
    }

    @Test
    void feeTableTest() throws Exception {
        RoomTable roomTable = getRoomTableRandomSampleGenerator();
        FeeTable feeTableBack = getFeeTableRandomSampleGenerator();

        roomTable.addFeeTable(feeTableBack);
        assertThat(roomTable.getFeeTables()).containsOnly(feeTableBack);
        assertThat(feeTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.removeFeeTable(feeTableBack);
        assertThat(roomTable.getFeeTables()).doesNotContain(feeTableBack);
        assertThat(feeTableBack.getRoomTable()).isNull();

        roomTable.feeTables(new HashSet<>(Set.of(feeTableBack)));
        assertThat(roomTable.getFeeTables()).containsOnly(feeTableBack);
        assertThat(feeTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.setFeeTables(new HashSet<>());
        assertThat(roomTable.getFeeTables()).doesNotContain(feeTableBack);
        assertThat(feeTableBack.getRoomTable()).isNull();
    }

    @Test
    void vehicleTableTest() throws Exception {
        RoomTable roomTable = getRoomTableRandomSampleGenerator();
        VehicleTable vehicleTableBack = getVehicleTableRandomSampleGenerator();

        roomTable.addVehicleTable(vehicleTableBack);
        assertThat(roomTable.getVehicleTables()).containsOnly(vehicleTableBack);
        assertThat(vehicleTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.removeVehicleTable(vehicleTableBack);
        assertThat(roomTable.getVehicleTables()).doesNotContain(vehicleTableBack);
        assertThat(vehicleTableBack.getRoomTable()).isNull();

        roomTable.vehicleTables(new HashSet<>(Set.of(vehicleTableBack)));
        assertThat(roomTable.getVehicleTables()).containsOnly(vehicleTableBack);
        assertThat(vehicleTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.setVehicleTables(new HashSet<>());
        assertThat(roomTable.getVehicleTables()).doesNotContain(vehicleTableBack);
        assertThat(vehicleTableBack.getRoomTable()).isNull();
    }

    @Test
    void billTableTest() throws Exception {
        RoomTable roomTable = getRoomTableRandomSampleGenerator();
        BillTable billTableBack = getBillTableRandomSampleGenerator();

        roomTable.addBillTable(billTableBack);
        assertThat(roomTable.getBillTables()).containsOnly(billTableBack);
        assertThat(billTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.removeBillTable(billTableBack);
        assertThat(roomTable.getBillTables()).doesNotContain(billTableBack);
        assertThat(billTableBack.getRoomTable()).isNull();

        roomTable.billTables(new HashSet<>(Set.of(billTableBack)));
        assertThat(roomTable.getBillTables()).containsOnly(billTableBack);
        assertThat(billTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.setBillTables(new HashSet<>());
        assertThat(roomTable.getBillTables()).doesNotContain(billTableBack);
        assertThat(billTableBack.getRoomTable()).isNull();
    }

    @Test
    void donationTableTest() throws Exception {
        RoomTable roomTable = getRoomTableRandomSampleGenerator();
        DonationTable donationTableBack = getDonationTableRandomSampleGenerator();

        roomTable.addDonationTable(donationTableBack);
        assertThat(roomTable.getDonationTables()).containsOnly(donationTableBack);
        assertThat(donationTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.removeDonationTable(donationTableBack);
        assertThat(roomTable.getDonationTables()).doesNotContain(donationTableBack);
        assertThat(donationTableBack.getRoomTable()).isNull();

        roomTable.donationTables(new HashSet<>(Set.of(donationTableBack)));
        assertThat(roomTable.getDonationTables()).containsOnly(donationTableBack);
        assertThat(donationTableBack.getRoomTable()).isEqualTo(roomTable);

        roomTable.setDonationTables(new HashSet<>());
        assertThat(roomTable.getDonationTables()).doesNotContain(donationTableBack);
        assertThat(donationTableBack.getRoomTable()).isNull();
    }

    @Test
    void citizenTableTest() throws Exception {
        RoomTable roomTable = getRoomTableRandomSampleGenerator();
        CitizenTable citizenTableBack = getCitizenTableRandomSampleGenerator();

        roomTable.setCitizenTable(citizenTableBack);
        assertThat(roomTable.getCitizenTable()).isEqualTo(citizenTableBack);
        assertThat(citizenTableBack.getFamilyId()).isEqualTo(roomTable);

        roomTable.citizenTable(null);
        assertThat(roomTable.getCitizenTable()).isNull();
        assertThat(citizenTableBack.getFamilyId()).isNull();
    }
}
