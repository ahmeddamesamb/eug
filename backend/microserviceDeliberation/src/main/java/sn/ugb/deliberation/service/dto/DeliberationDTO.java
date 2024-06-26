package sn.ugb.deliberation.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.deliberation.domain.Deliberation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliberationDTO implements Serializable {

    private Long id;

    private Boolean estValideeYN;

    @Lob
    private byte[] pvDeliberation;

    private String pvDeliberationContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstValideeYN() {
        return estValideeYN;
    }

    public void setEstValideeYN(Boolean estValideeYN) {
        this.estValideeYN = estValideeYN;
    }

    public byte[] getPvDeliberation() {
        return pvDeliberation;
    }

    public void setPvDeliberation(byte[] pvDeliberation) {
        this.pvDeliberation = pvDeliberation;
    }

    public String getPvDeliberationContentType() {
        return pvDeliberationContentType;
    }

    public void setPvDeliberationContentType(String pvDeliberationContentType) {
        this.pvDeliberationContentType = pvDeliberationContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliberationDTO)) {
            return false;
        }

        DeliberationDTO deliberationDTO = (DeliberationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliberationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliberationDTO{" +
            "id=" + getId() +
            ", estValideeYN='" + getEstValideeYN() + "'" +
            ", pvDeliberation='" + getPvDeliberation() + "'" +
            "}";
    }
}
