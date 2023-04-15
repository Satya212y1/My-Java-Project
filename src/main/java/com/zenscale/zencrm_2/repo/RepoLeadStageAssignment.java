package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.LeadStageAssignment;
import com.zenscale.zencrm_2.entity.LeadStageAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RepoLeadStageAssignment extends JpaRepository<LeadStageAssignment, LeadStageAssignmentId> {

    @Query("select coalesce(max (e.id.objid), 0) from LeadStageAssignment e where e.id.bukrs = :bukrs and e.id.leadId = :leadid")
    Integer getMaxObjid(@Param("bukrs") int bukrs,
                        @Param("leadid") int leadid);

    @Transactional
    @Modifying
    @Query("update LeadStageAssignment e set e.stats = 'X' where e.id.bukrs = :bukrs and e.id.leadId = :leadId and e.stats = ''")
    void updateStats(@Param("bukrs") Integer bukrs,
                     @Param("leadId") Integer leadId);

    boolean existsById_BukrsAndId_LeadIdAndStats(int bukrs, int leadid, String stats);

    boolean existsById_BukrsAndId_LeadIdAndStageIdAndStats(int bukrs, int leadid, int stageId, String stats);

    @Query("select coalesce(e.id.leadId, 0) from LeadStageAssignment e where e.id.bukrs = :bukrs and e.stageId = :stageId and e.stats = ''")
    List<Integer> findLeadIdByStageId(@Param("bukrs") int bukrs,
                             @Param("stageId") int stageId);




}