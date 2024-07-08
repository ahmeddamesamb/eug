package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InformationImage;

import java.util.Optional;

/**
 * Spring Data JPA repository for the InformationImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformationImageRepository extends JpaRepository<InformationImage, Long> {
    Optional<InformationImage> findByEnCoursYN(String EnCoursYN);
}
