package org.example.civic_govt.repository;

import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> , JpaSpecificationExecutor<Issue> {

    @Query("SELECT i FROM Issue i WHERE i.title = ?1 AND i.latitude = ?2 AND i.longitude = ?3")
    Optional<Issue> findByTitleAndLatitudeAndLongitude(String title, Double latitude, Double longitude);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Issue i WHERE i.id = ?1 AND i.zone.zoneHead.id = ?2")
    Boolean existsIssueByIdAndHeadId(Long issueId,Long headId);

    @Query("SELECT i.zone FROM Issue i WHERE i.id = ?1")
    Optional<Zone> findZoneById(Long issueId);

    @Query("SELECT i.reporters FROM Issue i WHERE i.id = ?1")
    List<User> findReportersByIssueId(Long id);
}

