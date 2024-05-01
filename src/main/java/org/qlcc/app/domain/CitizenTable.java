package org.qlcc.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CitizenTable.
 */
@Entity
@Table(name = "citizen_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitizenTable implements Serializable {

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
    @Column(name = "citizen_id", nullable = false)
    private String citizenID;

    @Column(name = "name")
    private String name;

    @Column(name = "dob")
    private String dob;

    @Column(name = "contact")
    private String contact;

    @Column(name = "gender")
    private String gender;

    @Column(name = "relation")
    private String relation;

    @Column(name = "status")
    private String status;

    @JsonIgnoreProperties(value = { "feeTables", "vehicleTables", "billTables", "donationTables", "citizenTable" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private RoomTable familyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CitizenTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public CitizenTable createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public CitizenTable updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public CitizenTable deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCitizenID() {
        return this.citizenID;
    }

    public CitizenTable citizenID(String citizenID) {
        this.setCitizenID(citizenID);
        return this;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getName() {
        return this.name;
    }

    public CitizenTable name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return this.dob;
    }

    public CitizenTable dob(String dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContact() {
        return this.contact;
    }

    public CitizenTable contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return this.gender;
    }

    public CitizenTable gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelation() {
        return this.relation;
    }

    public CitizenTable relation(String relation) {
        this.setRelation(relation);
        return this;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getStatus() {
        return this.status;
    }

    public CitizenTable status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoomTable getFamilyId() {
        return this.familyId;
    }

    public void setFamilyId(RoomTable roomTable) {
        this.familyId = roomTable;
    }

    public CitizenTable familyId(RoomTable roomTable) {
        this.setFamilyId(roomTable);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitizenTable)) {
            return false;
        }
        return getId() != null && getId().equals(((CitizenTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitizenTable{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", citizenID='" + getCitizenID() + "'" +
            ", name='" + getName() + "'" +
            ", dob='" + getDob() + "'" +
            ", contact='" + getContact() + "'" +
            ", gender='" + getGender() + "'" +
            ", relation='" + getRelation() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
