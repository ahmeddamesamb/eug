package sn.ugb.gir.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_execution")
    private Instant dateExecution;

    @Column(name = "email_user")
    private String emailUser;

    @Lob
    @Column(name = "detail_operation")
    private String detailOperation;

    @Column(name = "info_systeme")
    private String infoSysteme;

    @ManyToOne(fetch = FetchType.LAZY)
    private TypeOperation typeOperation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateExecution() {
        return this.dateExecution;
    }

    public Operation dateExecution(Instant dateExecution) {
        this.setDateExecution(dateExecution);
        return this;
    }

    public void setDateExecution(Instant dateExecution) {
        this.dateExecution = dateExecution;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public Operation emailUser(String emailUser) {
        this.setEmailUser(emailUser);
        return this;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getDetailOperation() {
        return this.detailOperation;
    }

    public Operation detailOperation(String detailOperation) {
        this.setDetailOperation(detailOperation);
        return this;
    }

    public void setDetailOperation(String detailOperation) {
        this.detailOperation = detailOperation;
    }

    public String getInfoSysteme() {
        return this.infoSysteme;
    }

    public Operation infoSysteme(String infoSysteme) {
        this.setInfoSysteme(infoSysteme);
        return this;
    }

    public void setInfoSysteme(String infoSysteme) {
        this.infoSysteme = infoSysteme;
    }

    public TypeOperation getTypeOperation() {
        return this.typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public Operation typeOperation(TypeOperation typeOperation) {
        this.setTypeOperation(typeOperation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operation)) {
            return false;
        }
        return getId() != null && getId().equals(((Operation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", dateExecution='" + getDateExecution() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            ", detailOperation='" + getDetailOperation() + "'" +
            ", infoSysteme='" + getInfoSysteme() + "'" +
            "}";
    }
}
