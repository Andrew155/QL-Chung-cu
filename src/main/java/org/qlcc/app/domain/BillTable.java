package org.qlcc.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BillTable.
 */
@Entity
@Table(name = "bill_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "bill_type")
    private String billType;

    @NotNull
    @Column(name = "bill_id", nullable = false)
    private String billId;

    @Column(name = "bill_month")
    private String billMonth;

    @Column(name = "bill_amount")
    private Integer billAmount;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "date")
    private Instant date;

    @Column(name = "status")
    private String status;

    @Column(name = "bill_cost")
    private Long billCost;

    @Column(name = "customer_id")
    private String customerID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feeTables", "vehicleTables", "billTables", "donationTables", "citizenTable" }, allowSetters = true)
    private RoomTable roomTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "billTables", "notificationTables" }, allowSetters = true)
    private ApplicationUser applicationTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public BillTable createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public BillTable updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public BillTable deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getBillType() {
        return this.billType;
    }

    public BillTable billType(String billType) {
        this.setBillType(billType);
        return this;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillId() {
        return this.billId;
    }

    public BillTable billId(String billId) {
        this.setBillId(billId);
        return this;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillMonth() {
        return this.billMonth;
    }

    public BillTable billMonth(String billMonth) {
        this.setBillMonth(billMonth);
        return this;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public Integer getBillAmount() {
        return this.billAmount;
    }

    public BillTable billAmount(Integer billAmount) {
        this.setBillAmount(billAmount);
        return this;
    }

    public void setBillAmount(Integer billAmount) {
        this.billAmount = billAmount;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public BillTable roomId(String roomId) {
        this.setRoomId(roomId);
        return this;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Instant getDate() {
        return this.date;
    }

    public BillTable date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getStatus() {
        return this.status;
    }

    public BillTable status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBillCost() {
        return this.billCost;
    }

    public BillTable billCost(Long billCost) {
        this.setBillCost(billCost);
        return this;
    }

    public void setBillCost(Long billCost) {
        this.billCost = billCost;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public BillTable customerID(String customerID) {
        this.setCustomerID(customerID);
        return this;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public RoomTable getRoomTable() {
        return this.roomTable;
    }

    public void setRoomTable(RoomTable roomTable) {
        this.roomTable = roomTable;
    }

    public BillTable roomTable(RoomTable roomTable) {
        this.setRoomTable(roomTable);
        return this;
    }

    public ApplicationUser getApplicationTable() {
        return this.applicationTable;
    }

    public void setApplicationTable(ApplicationUser applicationUser) {
        this.applicationTable = applicationUser;
    }

    public BillTable applicationTable(ApplicationUser applicationUser) {
        this.setApplicationTable(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillTable)) {
            return false;
        }
        return getId() != null && getId().equals(((BillTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillTable{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", billType='" + getBillType() + "'" +
            ", billId='" + getBillId() + "'" +
            ", billMonth='" + getBillMonth() + "'" +
            ", billAmount=" + getBillAmount() +
            ", roomId='" + getRoomId() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", billCost=" + getBillCost() +
            ", customerID='" + getCustomerID() + "'" +
            "}";
    }
}
