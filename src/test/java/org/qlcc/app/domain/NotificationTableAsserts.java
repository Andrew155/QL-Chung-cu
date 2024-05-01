package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationTableAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationTableAllPropertiesEquals(NotificationTable expected, NotificationTable actual) {
        assertNotificationTableAutoGeneratedPropertiesEquals(expected, actual);
        assertNotificationTableAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationTableAllUpdatablePropertiesEquals(NotificationTable expected, NotificationTable actual) {
        assertNotificationTableUpdatableFieldsEquals(expected, actual);
        assertNotificationTableUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationTableAutoGeneratedPropertiesEquals(NotificationTable expected, NotificationTable actual) {
        assertThat(expected)
            .as("Verify NotificationTable auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationTableUpdatableFieldsEquals(NotificationTable expected, NotificationTable actual) {
        assertThat(expected)
            .as("Verify NotificationTable relevant properties")
            .satisfies(e -> assertThat(e.getCreateAt()).as("check createAt").isEqualTo(actual.getCreateAt()))
            .satisfies(e -> assertThat(e.getUpdateAt()).as("check updateAt").isEqualTo(actual.getUpdateAt()))
            .satisfies(e -> assertThat(e.getDeletedAt()).as("check deletedAt").isEqualTo(actual.getDeletedAt()))
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getContent()).as("check content").isEqualTo(actual.getContent()))
            .satisfies(e -> assertThat(e.getUserID()).as("check userID").isEqualTo(actual.getUserID()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationTableUpdatableRelationshipsEquals(NotificationTable expected, NotificationTable actual) {
        assertThat(expected)
            .as("Verify NotificationTable relationships")
            .satisfies(e -> assertThat(e.getApplicationTable()).as("check applicationTable").isEqualTo(actual.getApplicationTable()));
    }
}