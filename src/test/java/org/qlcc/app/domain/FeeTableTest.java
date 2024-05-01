package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.FeeTableTestSamples.*;
import static org.qlcc.app.domain.RoomTableTestSamples.*;

import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class FeeTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeeTable.class);
        FeeTable feeTable1 = getFeeTableSample1();
        FeeTable feeTable2 = new FeeTable();
        assertThat(feeTable1).isNotEqualTo(feeTable2);

        feeTable2.setId(feeTable1.getId());
        assertThat(feeTable1).isEqualTo(feeTable2);

        feeTable2 = getFeeTableSample2();
        assertThat(feeTable1).isNotEqualTo(feeTable2);
    }

    @Test
    void roomTableTest() throws Exception {
        FeeTable feeTable = getFeeTableRandomSampleGenerator();
        RoomTable roomTableBack = getRoomTableRandomSampleGenerator();

        feeTable.setRoomTable(roomTableBack);
        assertThat(feeTable.getRoomTable()).isEqualTo(roomTableBack);

        feeTable.roomTable(null);
        assertThat(feeTable.getRoomTable()).isNull();
    }
}
