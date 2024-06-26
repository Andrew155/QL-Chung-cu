package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomTableAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomTableAllPropertiesEquals(RoomTable expected, RoomTable actual) {
        assertRoomTableAutoGeneratedPropertiesEquals(expected, actual);
        assertRoomTableAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomTableAllUpdatablePropertiesEquals(RoomTable expected, RoomTable actual) {
        assertRoomTableUpdatableFieldsEquals(expected, actual);
        assertRoomTableUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomTableAutoGeneratedPropertiesEquals(RoomTable expected, RoomTable actual) {
        assertThat(expected)
            .as("Verify RoomTable auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomTableUpdatableFieldsEquals(RoomTable expected, RoomTable actual) {
        assertThat(expected)
            .as("Verify RoomTable relevant properties")
            .satisfies(e -> assertThat(e.getCreateAt()).as("check createAt").isEqualTo(actual.getCreateAt()))
            .satisfies(e -> assertThat(e.getUpdateAt()).as("check updateAt").isEqualTo(actual.getUpdateAt()))
            .satisfies(e -> assertThat(e.getDeletedAt()).as("check deletedAt").isEqualTo(actual.getDeletedAt()))
            .satisfies(e -> assertThat(e.getRoomId()).as("check roomId").isEqualTo(actual.getRoomId()))
            .satisfies(e -> assertThat(e.getArea()).as("check area").isEqualTo(actual.getArea()))
            .satisfies(e -> assertThat(e.getOwnTime()).as("check ownTime").isEqualTo(actual.getOwnTime()))
            .satisfies(e -> assertThat(e.getOwnerId()).as("check ownerId").isEqualTo(actual.getOwnerId()))
            .satisfies(e -> assertThat(e.getOwnerName()).as("check ownerName").isEqualTo(actual.getOwnerName()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomTableUpdatableRelationshipsEquals(RoomTable expected, RoomTable actual) {}
}
