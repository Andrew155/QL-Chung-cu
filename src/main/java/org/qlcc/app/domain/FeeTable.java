package org.qlcc.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FeeTable.
 */
@Entity
@Table(name = "fee_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeeTable implements Serializable {

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

    @Column(name = "fee_type")
    private String feeType;

    @Column(name = "fee_desc")
    private String feeDesc;

    @Column(name = "fee_month")
    private String feeMonth;

    @Column(name = "fee_cost")
    private Long feeCost;

    @Column(name = "date")
    private Instant date;

    @Column(name = "status")
    private String status;

    @NotNull
    @Column(name = "fee_id", nullable = false)
    private String feeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feeTables", "vehicleTables", "billTables", "donationTables", "citizenTable" }, allowSetters = true)
    private RoomTable roomTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeeTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public FeeTable createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public FeeTable updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public FeeTable deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getFeeType() {
        return this.feeType;
    }

    public FeeTable feeType(String feeType) {
        this.setFeeType(feeType);
        return this;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeDesc() {
        return this.feeDesc;
    }

    public FeeTable feeDesc(String feeDesc) {
        this.setFeeDesc(feeDesc);
        return this;
    }

    public void setFeeDesc(String feeDesc) {
        this.feeDesc = feeDesc;
    }

    public String getFeeMonth() {
        return this.feeMonth;
    }

    public FeeTable feeMonth(String feeMonth) {
        this.setFeeMonth(feeMonth);
        return this;
    }

    public void setFeeMonth(String feeMonth) {
        this.feeMonth = feeMonth;
    }

    public Long getFeeCost() {
        return this.feeCost;
    }

    public FeeTable feeCost(Long feeCost) {
        this.setFeeCost(feeCost);
        return this;
    }

    public void setFeeCost(Long feeCost) {
        this.feeCost = feeCost;
    }

    public Instant getDate() {
        return this.date;
    }

    public FeeTable date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getStatus() {
        return this.status;
    }

    public FeeTable status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeeId() {
        return this.feeId;
    }

    public FeeTable feeId(String feeId) {
        this.setFeeId(feeId);
        return this;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public RoomTable getRoomTable() {
        return this.roomTable;
    }

    public void setRoomTable(RoomTable roomTable) {
        this.roomTable = roomTable;
    }

    public FeeTable roomTable(RoomTable roomTable) {
        this.setRoomTable(roomTable);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeeTable)) {
            return false;
        }
        return getId() != null && getId().equals(((FeeTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeeTable{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", feeType='" + getFeeType() + "'" +
            ", feeDesc='" + getFeeDesc() + "'" +
            ", feeMonth='" + getFeeMonth() + "'" +
            ", feeCost=" + getFeeCost() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", feeId='" + getFeeId() + "'" +
            "}";
    }
}
