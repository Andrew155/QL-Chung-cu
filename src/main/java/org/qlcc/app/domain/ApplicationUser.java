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
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "family_id")
    private String familyId;

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

    @Column(name = "room_id")
    private String roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User internalUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicationTable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomTable", "applicationTable" }, allowSetters = true)
    private Set<BillTable> billTables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicationTable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requestTable", "applicationTable" }, allowSetters = true)
    private Set<NotificationTable> notificationTables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public ApplicationUser createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public ApplicationUser updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public ApplicationUser deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getFamilyId() {
        return this.familyId;
    }

    public ApplicationUser familyId(String familyId) {
        this.setFamilyId(familyId);
        return this;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getCitizenID() {
        return this.citizenID;
    }

    public ApplicationUser citizenID(String citizenID) {
        this.setCitizenID(citizenID);
        return this;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getName() {
        return this.name;
    }

    public ApplicationUser name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return this.dob;
    }

    public ApplicationUser dob(String dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContact() {
        return this.contact;
    }

    public ApplicationUser contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return this.gender;
    }

    public ApplicationUser gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelation() {
        return this.relation;
    }

    public ApplicationUser relation(String relation) {
        this.setRelation(relation);
        return this;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getStatus() {
        return this.status;
    }

    public ApplicationUser status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public ApplicationUser roomId(String roomId) {
        this.setRoomId(roomId);
        return this;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<BillTable> getBillTables() {
        return this.billTables;
    }

    public void setBillTables(Set<BillTable> billTables) {
        if (this.billTables != null) {
            this.billTables.forEach(i -> i.setApplicationTable(null));
        }
        if (billTables != null) {
            billTables.forEach(i -> i.setApplicationTable(this));
        }
        this.billTables = billTables;
    }

    public ApplicationUser billTables(Set<BillTable> billTables) {
        this.setBillTables(billTables);
        return this;
    }

    public ApplicationUser addBillTable(BillTable billTable) {
        this.billTables.add(billTable);
        billTable.setApplicationTable(this);
        return this;
    }

    public ApplicationUser removeBillTable(BillTable billTable) {
        this.billTables.remove(billTable);
        billTable.setApplicationTable(null);
        return this;
    }

    public Set<NotificationTable> getNotificationTables() {
        return this.notificationTables;
    }

    public void setNotificationTables(Set<NotificationTable> notificationTables) {
        if (this.notificationTables != null) {
            this.notificationTables.forEach(i -> i.setApplicationTable(null));
        }
        if (notificationTables != null) {
            notificationTables.forEach(i -> i.setApplicationTable(this));
        }
        this.notificationTables = notificationTables;
    }

    public ApplicationUser notificationTables(Set<NotificationTable> notificationTables) {
        this.setNotificationTables(notificationTables);
        return this;
    }

    public ApplicationUser addNotificationTable(NotificationTable notificationTable) {
        this.notificationTables.add(notificationTable);
        notificationTable.setApplicationTable(this);
        return this;
    }

    public ApplicationUser removeNotificationTable(NotificationTable notificationTable) {
        this.notificationTables.remove(notificationTable);
        notificationTable.setApplicationTable(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", familyId='" + getFamilyId() + "'" +
            ", citizenID='" + getCitizenID() + "'" +
            ", name='" + getName() + "'" +
            ", dob='" + getDob() + "'" +
            ", contact='" + getContact() + "'" +
            ", gender='" + getGender() + "'" +
            ", relation='" + getRelation() + "'" +
            ", status='" + getStatus() + "'" +
            ", roomId='" + getRoomId() + "'" +
            "}";
    }
}
