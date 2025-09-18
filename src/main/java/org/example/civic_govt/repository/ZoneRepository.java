package org.example.civic_govt.repository;

import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ZoneRepository extends JpaRepository<Zone, Long>, JpaSpecificationExecutor<Zone> {

    @Query("SELECT z FROM Zone z WHERE z.district = ?1 AND z.name = ?2")
    Zone findZoneByDistrictName(District district, String zoneName);

    @Query("SELECT z.subordinateOfficials FROM Zone z where z.id= ?1")
    Page<User> findOfficialsByZoneId(Long id, Pageable pageable);

    @Query("SELECT z FROM Zone z WHERE z.district.id = ?1")
    Page<Zone> findZonesByDistrictId(Long id, Pageable pageable, Specification<Zone> spec);
}
