package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.AlertsAssign;
import com.zenscale.zencrm_2.entity.AlertsAssignId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoAlertsAssign extends JpaRepository<AlertsAssign, AlertsAssignId> {

    @Query("select coalesce(max (e.id.alertCounter), 0) from AlertsAssign e where e.id.bukrs = :bukrs and e.id.alertId = :alertId")
    Integer getMaxAlertAssignCounter(@Param("bukrs") int bukrs,
                                     @Param("alertId") int alertId);




}