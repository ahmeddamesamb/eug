package sn.ugb.gir.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Operation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperationDTO implements Serializable {

    private Long id;

    private Instant dateExecution;

    private String emailUser;

    @Lob
    private String detailOperation;

    private String infoSysteme;

    private TypeOperationDTO typeOperation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(Instant dateExecution) {
        this.dateExecution = dateExecution;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getDetailOperation() {
        return detailOperation;
    }

    public void setDetailOperation(String detailOperation) {
        this.detailOperation = detailOperation;
    }

    public String getInfoSysteme() {
        return infoSysteme;
    }

    public void setInfoSysteme(String infoSysteme) {
        this.infoSysteme = infoSysteme;
    }

    public TypeOperationDTO getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperationDTO typeOperation) {
        this.typeOperation = typeOperation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationDTO)) {
            return false;
        }

        OperationDTO operationDTO = (OperationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationDTO{" +
            "id=" + getId() +
            ", dateExecution='" + getDateExecution() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            ", detailOperation='" + getDetailOperation() + "'" +
            ", infoSysteme='" + getInfoSysteme() + "'" +
            ", typeOperation=" + getTypeOperation() +
            "}";
    }
}
