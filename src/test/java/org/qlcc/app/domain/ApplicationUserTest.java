package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qlcc.app.domain.ApplicationUserTestSamples.*;
import static org.qlcc.app.domain.BillTableTestSamples.*;
import static org.qlcc.app.domain.NotificationTableTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.qlcc.app.web.rest.TestUtil;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void billTableTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        BillTable billTableBack = getBillTableRandomSampleGenerator();

        applicationUser.addBillTable(billTableBack);
        assertThat(applicationUser.getBillTables()).containsOnly(billTableBack);
        assertThat(billTableBack.getApplicationTable()).isEqualTo(applicationUser);

        applicationUser.removeBillTable(billTableBack);
        assertThat(applicationUser.getBillTables()).doesNotContain(billTableBack);
        assertThat(billTableBack.getApplicationTable()).isNull();

        applicationUser.billTables(new HashSet<>(Set.of(billTableBack)));
        assertThat(applicationUser.getBillTables()).containsOnly(billTableBack);
        assertThat(billTableBack.getApplicationTable()).isEqualTo(applicationUser);

        applicationUser.setBillTables(new HashSet<>());
        assertThat(applicationUser.getBillTables()).doesNotContain(billTableBack);
        assertThat(billTableBack.getApplicationTable()).isNull();
    }

    @Test
    void notificationTableTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        NotificationTable notificationTableBack = getNotificationTableRandomSampleGenerator();

        applicationUser.addNotificationTable(notificationTableBack);
        assertThat(applicationUser.getNotificationTables()).containsOnly(notificationTableBack);
        assertThat(notificationTableBack.getApplicationTable()).isEqualTo(applicationUser);

        applicationUser.removeNotificationTable(notificationTableBack);
        assertThat(applicationUser.getNotificationTables()).doesNotContain(notificationTableBack);
        assertThat(notificationTableBack.getApplicationTable()).isNull();

        applicationUser.notificationTables(new HashSet<>(Set.of(notificationTableBack)));
        assertThat(applicationUser.getNotificationTables()).containsOnly(notificationTableBack);
        assertThat(notificationTableBack.getApplicationTable()).isEqualTo(applicationUser);

        applicationUser.setNotificationTables(new HashSet<>());
        assertThat(applicationUser.getNotificationTables()).doesNotContain(notificationTableBack);
        assertThat(notificationTableBack.getApplicationTable()).isNull();
    }
}
