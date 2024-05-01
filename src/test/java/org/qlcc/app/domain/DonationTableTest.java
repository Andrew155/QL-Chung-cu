package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.DonationTableTestSamples.*;
import static org.qlcc.app.domain.RoomTableTestSamples.*;

import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class DonationTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonationTable.class);
        DonationTable donationTable1 = getDonationTableSample1();
        DonationTable donationTable2 = new DonationTable();
        assertThat(donationTable1).isNotEqualTo(donationTable2);

        donationTable2.setId(donationTable1.getId());
        assertThat(donationTable1).isEqualTo(donationTable2);

        donationTable2 = getDonationTableSample2();
        assertThat(donationTable1).isNotEqualTo(donationTable2);
    }

    @Test
    void roomTableTest() throws Exception {
        DonationTable donationTable = getDonationTableRandomSampleGenerator();
        RoomTable roomTableBack = getRoomTableRandomSampleGenerator();

        donationTable.setRoomTable(roomTableBack);
        assertThat(donationTable.getRoomTable()).isEqualTo(roomTableBack);

        donationTable.roomTable(null);
        assertThat(donationTable.getRoomTable()).isNull();
    }
}
