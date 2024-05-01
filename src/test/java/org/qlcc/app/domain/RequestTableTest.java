package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.NotificationTableTestSamples.*;
import static org.qlcc.app.domain.RequestTableTestSamples.*;

import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class RequestTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestTable.class);
        RequestTable requestTable1 = getRequestTableSample1();
        RequestTable requestTable2 = new RequestTable();
        assertThat(requestTable1).isNotEqualTo(requestTable2);

        requestTable2.setId(requestTable1.getId());
        assertThat(requestTable1).isEqualTo(requestTable2);

        requestTable2 = getRequestTableSample2();
        assertThat(requestTable1).isNotEqualTo(requestTable2);
    }

    @Test
    void notificationTableTest() throws Exception {
        RequestTable requestTable = getRequestTableRandomSampleGenerator();
        NotificationTable notificationTableBack = getNotificationTableRandomSampleGenerator();

        requestTable.setNotificationTable(notificationTableBack);
        assertThat(requestTable.getNotificationTable()).isEqualTo(notificationTableBack);

        requestTable.notificationTable(null);
        assertThat(requestTable.getNotificationTable()).isNull();
    }
}
