package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.AlertsI;
import com.zenscale.zencrm_2.entity.AlertsIId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RepoAlertsI extends JpaRepository<AlertsI, AlertsIId> {

    @Query("select coalesce(max (e.id.counter), 0) from AlertsI e where e.id.bukrs = :bukrs and e.id.alertId = :alertId")
    Integer getMaxAlertICounter(@Param("bukrs") int bukrs,
                                @Param("alertId") int alertId);

    @Transactional
    @Modifying
    @Query("update AlertsI e set e.status = :status where e.id.bukrs = :bukrs and e.id.alertId = :alertId and e.id.counter = :counter")
    void updateAlertStatus(@Param("bukrs") Integer bukrs,
                           @Param("alertId") Integer alertId,
                           @Param("counter") Integer counter,
                           @Param("status") String status);

    boolean existsById_BukrsAndId_AlertIdAndId_Counter(int bukrs, int alertId, int counter);


}