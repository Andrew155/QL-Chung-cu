package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.ApplicationUserTestSamples.*;
import static org.qlcc.app.domain.BillTableTestSamples.*;
import static org.qlcc.app.domain.RoomTableTestSamples.*;

import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class BillTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillTable.class);
        BillTable billTable1 = getBillTableSample1();
        BillTable billTable2 = new BillTable();
        assertThat(billTable1).isNotEqualTo(billTable2);

        billTable2.setId(billTable1.getId());
        assertThat(billTable1).isEqualTo(billTable2);

        billTable2 = getBillTableSample2();
        assertThat(billTable1).isNotEqualTo(billTable2);
    }

    @Test
    void roomTableTest() throws Exception {
        BillTable billTable = getBillTableRandomSampleGenerator();
        RoomTable roomTableBack = getRoomTableRandomSampleGenerator();

        billTable.setRoomTable(roomTableBack);
        assertThat(billTable.getRoomTable()).isEqualTo(roomTableBack);

        billTable.roomTable(null);
        assertThat(billTable.getRoomTable()).isNull();
    }

    @Test
    void applicationTableTest() throws Exception {
        BillTable billTable = getBillTableRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        billTable.setApplicationTable(applicationUserBack);
        assertThat(billTable.getApplicationTable()).isEqualTo(applicationUserBack);

        billTable.applicationTable(null);
        assertThat(billTable.getApplicationTable()).isNull();
    }
}
