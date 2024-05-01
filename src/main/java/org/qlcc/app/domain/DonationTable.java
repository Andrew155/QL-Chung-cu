package org.qlcc.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DonationTable.
 */
@Entity
@Table(name = "donation_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonationTable implements Serializable {

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

    @NotNull
    @Column(name = "donation_id", nullable = false)
    private String donationId;

    @Column(name = "donation_type")
    private String donationType;

    @Column(name = "donation_desc")
    private String donationDesc;

    @Column(name = "donation_month")
    private String donationMonth;

    @Column(name = "donation_cost")
    private Long donationCost;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feeTables", "vehicleTables", "billTables", "donationTables", "citizenTable" }, allowSetters = true)
    private RoomTable roomTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DonationTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public DonationTable createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public DonationTable updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public DonationTable deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDonationId() {
        return this.donationId;
    }

    public DonationTable donationId(String donationId) {
        this.setDonationId(donationId);
        return this;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getDonationType() {
        return this.donationType;
    }

    public DonationTable donationType(String donationType) {
        this.setDonationType(donationType);
        return this;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getDonationDesc() {
        return this.donationDesc;
    }

    public DonationTable donationDesc(String donationDesc) {
        this.setDonationDesc(donationDesc);
        return this;
    }

    public void setDonationDesc(String donationDesc) {
        this.donationDesc = donationDesc;
    }

    public String getDonationMonth() {
        return this.donationMonth;
    }

    public DonationTable donationMonth(String donationMonth) {
        this.setDonationMonth(donationMonth);
        return this;
    }

    public void setDonationMonth(String donationMonth) {
        this.donationMonth = donationMonth;
    }

    public Long getDonationCost() {
        return this.donationCost;
    }

    public DonationTable donationCost(Long donationCost) {
        this.setDonationCost(donationCost);
        return this;
    }

    public void setDonationCost(Long donationCost) {
        this.donationCost = donationCost;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public DonationTable roomId(String roomId) {
        this.setRoomId(roomId);
        return this;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return this.status;
    }

    public DonationTable status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoomTable getRoomTable() {
        return this.roomTable;
    }

    public void setRoomTable(RoomTable roomTable) {
        this.roomTable = roomTable;
    }

    public DonationTable roomTable(RoomTable roomTable) {
        this.setRoomTable(roomTable);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonationTable)) {
            return false;
        }
        return getId() != null && getId().equals(((DonationTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationTable{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", donationId='" + getDonationId() + "'" +
            ", donationType='" + getDonationType() + "'" +
            ", donationDesc='" + getDonationDesc() + "'" +
            ", donationMonth='" + getDonationMonth() + "'" +
            ", donationCost=" + getDonationCost() +
            ", roomId='" + getRoomId() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
