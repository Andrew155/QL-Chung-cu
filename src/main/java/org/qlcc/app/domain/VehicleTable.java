package org.qlcc.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleTable.
 */
@Entity
@Table(name = "vehicle_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleTable implements Serializable {

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

    @Column(name = "vehicle_name")
    private String vehicleName;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @NotNull
    @Column(name = "vehicle_id", nullable = false)
    private String vehicleId;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "vehicle_fee")
    private Long vehicleFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feeTables", "vehicleTables", "billTables", "donationTables", "citizenTable" }, allowSetters = true)
    private RoomTable roomTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public VehicleTable createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public VehicleTable updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public VehicleTable deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getVehicleName() {
        return this.vehicleName;
    }

    public VehicleTable vehicleName(String vehicleName) {
        this.setVehicleName(vehicleName);
        return this;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleType() {
        return this.vehicleType;
    }

    public VehicleTable vehicleType(String vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public VehicleTable vehicleId(String vehicleId) {
        this.setVehicleId(vehicleId);
        return this;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public VehicleTable roomId(String roomId) {
        this.setRoomId(roomId);
        return this;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public VehicleTable ownerId(String ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Long getVehicleFee() {
        return this.vehicleFee;
    }

    public VehicleTable vehicleFee(Long vehicleFee) {
        this.setVehicleFee(vehicleFee);
        return this;
    }

    public void setVehicleFee(Long vehicleFee) {
        this.vehicleFee = vehicleFee;
    }

    public RoomTable getRoomTable() {
        return this.roomTable;
    }

    public void setRoomTable(RoomTable roomTable) {
        this.roomTable = roomTable;
    }

    public VehicleTable roomTable(RoomTable roomTable) {
        this.setRoomTable(roomTable);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleTable)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleTable{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", vehicleName='" + getVehicleName() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", vehicleId='" + getVehicleId() + "'" +
            ", roomId='" + getRoomId() + "'" +
            ", ownerId='" + getOwnerId() + "'" +
            ", vehicleFee=" + getVehicleFee() +
            "}";
    }
}
