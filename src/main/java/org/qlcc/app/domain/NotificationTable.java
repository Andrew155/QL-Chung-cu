package org.qlcc.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NotificationTable.
 */
@Entity
@Table(name = "notification_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "user_id")
    private String userID;

    @JsonIgnoreProperties(value = { "notificationTable" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "notificationTable")
    private RequestTable requestTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "billTables", "notificationTables" }, allowSetters = true)
    private ApplicationUser applicationTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public NotificationTable id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public NotificationTable createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public NotificationTable updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public NotificationTable deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getTitle() {
        return this.title;
    }

    public NotificationTable title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public NotificationTable content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return this.userID;
    }

    public NotificationTable userID(String userID) {
        this.setUserID(userID);
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public RequestTable getRequestTable() {
        return this.requestTable;
    }

    public void setRequestTable(RequestTable requestTable) {
        if (this.requestTable != null) {
            this.requestTable.setNotificationTable(null);
        }
        if (requestTable != null) {
            requestTable.setNotificationTable(this);
        }
        this.requestTable = requestTable;
    }

    public NotificationTable requestTable(RequestTable requestTable) {
        this.setRequestTable(requestTable);
        return this;
    }

    public ApplicationUser getApplicationTable() {
        return this.applicationTable;
    }

    public void setApplicationTable(ApplicationUser applicationUser) {
        this.applicationTable = applicationUser;
    }

    public NotificationTable applicationTable(ApplicationUser applicationUser) {
        this.setApplicationTable(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationTable)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationTable{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", userID='" + getUserID() + "'" +
            "}";
    }
}
