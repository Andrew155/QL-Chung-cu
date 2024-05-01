package org.qlcc.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RoomTable.
 */
@Entity
@Table(name = "room_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomTable implements Serializable {

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
    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Column(name = "area")
    private String area;

    @Column(name = "own_time")
    private String ownTime;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomTable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomTable" }, allowSetters = true)
    private Set<FeeTable> feeTables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomTable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomTable" }, allowSetters = true)
    private Set<VehicleTable> vehicleTables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomTable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomTable", "applicationTable" }, allowSetters = true)
    private Set<BillTable> billTables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomTable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomTable" }, allowSetters = true)
    private Set<DonationTable> donationTables = new HashSet<>();

    @JsonIgnoreProperties(value = { "familyId" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "familyId")
    private CitizenTable citizenTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoomTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public RoomTable createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public RoomTable updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public RoomTable deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public RoomTable roomId(String roomId) {
        this.setRoomId(roomId);
        return this;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getArea() {
        return this.area;
    }

    public RoomTable area(String area) {
        this.setArea(area);
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOwnTime() {
        return this.ownTime;
    }

    public RoomTable ownTime(String ownTime) {
        this.setOwnTime(ownTime);
        return this;
    }

    public void setOwnTime(String ownTime) {
        this.ownTime = ownTime;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public RoomTable ownerId(String ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public RoomTable ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatus() {
        return this.status;
    }

    public RoomTable status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<FeeTable> getFeeTables() {
        return this.feeTables;
    }

    public void setFeeTables(Set<FeeTable> feeTables) {
        if (this.feeTables != null) {
            this.feeTables.forEach(i -> i.setRoomTable(null));
        }
        if (feeTables != null) {
            feeTables.forEach(i -> i.setRoomTable(this));
        }
        this.feeTables = feeTables;
    }

    public RoomTable feeTables(Set<FeeTable> feeTables) {
        this.setFeeTables(feeTables);
        return this;
    }

    public RoomTable addFeeTable(FeeTable feeTable) {
        this.feeTables.add(feeTable);
        feeTable.setRoomTable(this);
        return this;
    }

    public RoomTable removeFeeTable(FeeTable feeTable) {
        this.feeTables.remove(feeTable);
        feeTable.setRoomTable(null);
        return this;
    }

    public Set<VehicleTable> getVehicleTables() {
        return this.vehicleTables;
    }

    public void setVehicleTables(Set<VehicleTable> vehicleTables) {
        if (this.vehicleTables != null) {
            this.vehicleTables.forEach(i -> i.setRoomTable(null));
        }
        if (vehicleTables != null) {
            vehicleTables.forEach(i -> i.setRoomTable(this));
        }
        this.vehicleTables = vehicleTables;
    }

    public RoomTable vehicleTables(Set<VehicleTable> vehicleTables) {
        this.setVehicleTables(vehicleTables);
        return this;
    }

    public RoomTable addVehicleTable(VehicleTable vehicleTable) {
        this.vehicleTables.add(vehicleTable);
        vehicleTable.setRoomTable(this);
        return this;
    }

    public RoomTable removeVehicleTable(VehicleTable vehicleTable) {
        this.vehicleTables.remove(vehicleTable);
        vehicleTable.setRoomTable(null);
        return this;
    }

    public Set<BillTable> getBillTables() {
        return this.billTables;
    }

    public void setBillTables(Set<BillTable> billTables) {
        if (this.billTables != null) {
            this.billTables.forEach(i -> i.setRoomTable(null));
        }
        if (billTables != null) {
            billTables.forEach(i -> i.setRoomTable(this));
        }
        this.billTables = billTables;
    }

    public RoomTable billTables(Set<BillTable> billTables) {
        this.setBillTables(billTables);
        return this;
    }

    public RoomTable addBillTable(BillTable billTable) {
        this.billTables.add(billTable);
        billTable.setRoomTable(this);
        return this;
    }

    public RoomTable removeBillTable(BillTable billTable) {
        this.billTables.remove(billTable);
        billTable.setRoomTable(null);
        return this;
    }

    public Set<DonationTable> getDonationTables() {
        return this.donationTables;
    }

    public void setDonationTables(Set<DonationTable> donationTables) {
        if (this.donationTables != null) {
            this.donationTables.forEach(i -> i.setRoomTable(null));
        }
        if (donationTables != null) {
            donationTables.forEach(i -> i.setRoomTable(this));
        }
        this.donationTables = donationTables;
    }

    public RoomTable donationTables(Set<DonationTable> donationTables) {
        this.setDonationTables(donationTables);
        return this;
    }

    public RoomTable addDonationTable(DonationTable donationTable) {
        this.donationTables.add(donationTable);
        donationTable.setRoomTable(this);
        return this;
    }

    public RoomTable removeDonationTable(DonationTable donationTable) {
        this.donationTables.remove(donationTable);
        donationTable.setRoomTable(null);
        return this;
    }

    public CitizenTable getCitizenTable() {
        return this.citizenTable;
    }

    public void setCitizenTable(CitizenTable citizenTable) {
        if (this.citizenTable != null) {
            this.citizenTable.setFamilyId(null);
        }
        if (citizenTable != null) {
            citizenTable.setFamilyId(this);
        }
        this.citizenTable = citizenTable;
    }

    public RoomTable citizenTable(CitizenTable citizenTable) {
        this.setCitizenTable(citizenTable);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomTable)) {
            return false;
        }
        return getId() != null && getId().equals(((RoomTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomTable{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", roomId='" + getRoomId() + "'" +
            ", area='" + getArea() + "'" +
            ", ownTime='" + getOwnTime() + "'" +
            ", ownerId='" + getOwnerId() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
