package org.qlcc.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BillTableAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillTableAllPropertiesEquals(BillTable expected, BillTable actual) {
        assertBillTableAutoGeneratedPropertiesEquals(expected, actual);
        assertBillTableAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillTableAllUpdatablePropertiesEquals(BillTable expected, BillTable actual) {
        assertBillTableUpdatableFieldsEquals(expected, actual);
        assertBillTableUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillTableAutoGeneratedPropertiesEquals(BillTable expected, BillTable actual) {
        assertThat(expected)
            .as("Verify BillTable auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillTableUpdatableFieldsEquals(BillTable expected, BillTable actual) {
        assertThat(expected)
            .as("Verify BillTable relevant properties")
            .satisfies(e -> assertThat(e.getCreateAt()).as("check createAt").isEqualTo(actual.getCreateAt()))
            .satisfies(e -> assertThat(e.getUpdateAt()).as("check updateAt").isEqualTo(actual.getUpdateAt()))
            .satisfies(e -> assertThat(e.getDeletedAt()).as("check deletedAt").isEqualTo(actual.getDeletedAt()))
            .satisfies(e -> assertThat(e.getBillType()).as("check billType").isEqualTo(actual.getBillType()))
            .satisfies(e -> assertThat(e.getBillId()).as("check billId").isEqualTo(actual.getBillId()))
            .satisfies(e -> assertThat(e.getBillMonth()).as("check billMonth").isEqualTo(actual.getBillMonth()))
            .satisfies(e -> assertThat(e.getBillAmount()).as("check billAmount").isEqualTo(actual.getBillAmount()))
            .satisfies(e -> assertThat(e.getRoomId()).as("check roomId").isEqualTo(actual.getRoomId()))
            .satisfies(e -> assertThat(e.getDate()).as("check date").isEqualTo(actual.getDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getBillCost()).as("check billCost").isEqualTo(actual.getBillCost()))
            .satisfies(e -> assertThat(e.getCustomerID()).as("check customerID").isEqualTo(actual.getCustomerID()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillTableUpdatableRelationshipsEquals(BillTable expected, BillTable actual) {
        assertThat(expected)
            .as("Verify BillTable relationships")
            .satisfies(e -> assertThat(e.getRoomTable()).as("check roomTable").isEqualTo(actual.getRoomTable()))
            .satisfies(e -> assertThat(e.getApplicationTable()).as("check applicationTable").isEqualTo(actual.getApplicationTable()));
    }
}