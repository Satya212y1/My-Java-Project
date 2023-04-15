package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.LeadId;
import com.zenscale.zencrm_2.entity.Leads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RepoLeads extends JpaRepository<Leads, LeadId> {

    @Query("select coalesce(max (e.id.leadId), 0) from Leads e where e.id.bukrs = :bukrs")
    int getMaxId(@Param("bukrs") int bukrs);

    @Query("select coalesce(max (e.id.objid), 0) from Leads e where e.id.bukrs = :bukrs and e.id.leadId = :leadid")
    int getMaxObjid(@Param("bukrs") int bukrs,
                    @Param("leadid") int leadid);

    @Query("select coalesce(e.status, '') from Leads e where e.id.bukrs = :bukrs and e.id.leadId = :leadid and e.stats = '' and e.delstats = ''")
    String getstatus(@Param("bukrs") int bukrs,
                     @Param("leadid") int leadid);

    @Transactional
    @Modifying
    @Query("update Leads e set e.delstats = 'X' where e.id.bukrs = :bukrs and e.id.leadId = :leadid and e.stats = '' and e.delstats = ''")
    void updateDelStats(@Param("bukrs") Integer bukrs,
                        @Param("leadid") Integer leadid);

    @Transactional
    @Modifying
    @Query("update Leads e set e.stats = 'X' where e.id.bukrs = :bukrs and e.id.leadId = :leadid and e.stats = '' and e.delstats = ''")
    void updateStats(@Param("bukrs") Integer bukrs,
                     @Param("leadid") Integer leadid);

    @Transactional
    @Modifying
    @Query("update Leads e set e.status =:status where e.id.bukrs = :bukrs and e.id.leadId = :leadid and e.stats = '' and e.delstats = ''")
    void updateStatus(@Param("bukrs") Integer bukrs,
                      @Param("leadid") Integer leadid,
                      @Param("status") String status);

    boolean existsById_BukrsAndId_LeadIdAndDelstatsAndStats(int bukrs, int leadid, String delstats, String stats);

    Leads findById_BukrsAndId_LeadIdAndDelstatsAndStats(int bukrs, int leadid, String delstats, String stats);




}