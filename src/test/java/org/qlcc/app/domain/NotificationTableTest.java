package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.ApplicationUserTestSamples.*;
import static org.qlcc.app.domain.NotificationTableTestSamples.*;
import static org.qlcc.app.domain.RequestTableTestSamples.*;

import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class NotificationTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationTable.class);
        NotificationTable notificationTable1 = getNotificationTableSample1();
        NotificationTable notificationTable2 = new NotificationTable();
        assertThat(notificationTable1).isNotEqualTo(notificationTable2);

        notificationTable2.setId(notificationTable1.getId());
        assertThat(notificationTable1).isEqualTo(notificationTable2);

        notificationTable2 = getNotificationTableSample2();
        assertThat(notificationTable1).isNotEqualTo(notificationTable2);
    }

    @Test
    void requestTableTest() throws Exception {
        NotificationTable notificationTable = getNotificationTableRandomSampleGenerator();
        RequestTable requestTableBack = getRequestTableRandomSampleGenerator();

        notificationTable.setRequestTable(requestTableBack);
        assertThat(notificationTable.getRequestTable()).isEqualTo(requestTableBack);
        assertThat(requestTableBack.getNotificationTable()).isEqualTo(notificationTable);

        notificationTable.requestTable(null);
        assertThat(notificationTable.getRequestTable()).isNull();
        assertThat(requestTableBack.getNotificationTable()).isNull();
    }

    @Test
    void applicationTableTest() throws Exception {
        NotificationTable notificationTable = getNotificationTableRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        notificationTable.setApplicationTable(applicationUserBack);
        assertThat(notificationTable.getApplicationTable()).isEqualTo(applicationUserBack);

        notificationTable.applicationTable(null);
        assertThat(notificationTable.getApplicationTable()).isNull();
    }
}
