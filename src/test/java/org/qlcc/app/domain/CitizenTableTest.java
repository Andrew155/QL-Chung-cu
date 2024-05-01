package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.CitizenTableTestSamples.*;
import static org.qlcc.app.domain.RoomTableTestSamples.*;

import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class CitizenTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitizenTable.class);
        CitizenTable citizenTable1 = getCitizenTableSample1();
        CitizenTable citizenTable2 = new CitizenTable();
        assertThat(citizenTable1).isNotEqualTo(citizenTable2);

        citizenTable2.setId(citizenTable1.getId());
        assertThat(citizenTable1).isEqualTo(citizenTable2);

        citizenTable2 = getCitizenTableSample2();
        assertThat(citizenTable1).isNotEqualTo(citizenTable2);
    }

    @Test
    void familyIdTest() throws Exception {
        CitizenTable citizenTable = getCitizenTableRandomSampleGenerator();
        RoomTable roomTableBack = getRoomTableRandomSampleGenerator();

        citizenTable.setFamilyId(roomTableBack);
        assertThat(citizenTable.getFamilyId()).isEqualTo(roomTableBack);

        citizenTable.familyId(null);
        assertThat(citizenTable.getFamilyId()).isNull();
    }
}
