package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FeeTableAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeeTableAllPropertiesEquals(FeeTable expected, FeeTable actual) {
        assertFeeTableAutoGeneratedPropertiesEquals(expected, actual);
        assertFeeTableAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeeTableAllUpdatablePropertiesEquals(FeeTable expected, FeeTable actual) {
        assertFeeTableUpdatableFieldsEquals(expected, actual);
        assertFeeTableUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeeTableAutoGeneratedPropertiesEquals(FeeTable expected, FeeTable actual) {
        assertThat(expected)
            .as("Verify FeeTable auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeeTableUpdatableFieldsEquals(FeeTable expected, FeeTable actual) {
        assertThat(expected)
            .as("Verify FeeTable relevant properties")
            .satisfies(e -> assertThat(e.getCreateAt()).as("check createAt").isEqualTo(actual.getCreateAt()))
            .satisfies(e -> assertThat(e.getUpdateAt()).as("check updateAt").isEqualTo(actual.getUpdateAt()))
            .satisfies(e -> assertThat(e.getDeletedAt()).as("check deletedAt").isEqualTo(actual.getDeletedAt()))
            .satisfies(e -> assertThat(e.getFeeType()).as("check feeType").isEqualTo(actual.getFeeType()))
            .satisfies(e -> assertThat(e.getFeeDesc()).as("check feeDesc").isEqualTo(actual.getFeeDesc()))
            .satisfies(e -> assertThat(e.getFeeMonth()).as("check feeMonth").isEqualTo(actual.getFeeMonth()))
            .satisfies(e -> assertThat(e.getFeeCost()).as("check feeCost").isEqualTo(actual.getFeeCost()))
            .satisfies(e -> assertThat(e.getDate()).as("check date").isEqualTo(actual.getDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getFeeId()).as("check feeId").isEqualTo(actual.getFeeId()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeeTableUpdatableRelationshipsEquals(FeeTable expected, FeeTable actual) {
        assertThat(expected)
            .as("Verify FeeTable relationships")
            .satisfies(e -> assertThat(e.getRoomTable()).as("check roomTable").isEqualTo(actual.getRoomTable()));
    }
}
