package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.AlertsH;
import com.zenscale.zencrm_2.entity.AlertsHId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoAlertsH extends JpaRepository<AlertsH, AlertsHId> {

    @Query("select coalesce(max (e.id.alertId), 0) from AlertsH e where e.id.bukrs = :bukrs")
    Integer getMaxAlertId(@Param("bukrs") int bukrs);

}