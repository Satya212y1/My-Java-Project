package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.StagesM;
import com.zenscale.zencrm_2.entity.StagesMId;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RepoStagesM extends JpaRepository<StagesM, StagesMId> {

    @Query("select coalesce(max (e.id.stageId), 0) from StagesM e where e.id.bukrs = :bukrs")
    int getMaxStageId(@Param("bukrs") int bukrs);

    @Transactional
    @Modifying
    @Query("update StagesM e set e.delstats = 'X' where e.id.bukrs = :bukrs and e.id.stageId = :stageid")
    void updateDelStats(@Param("bukrs")  Integer bukrs,
                        @Param("stageid")  Integer stageid);

    boolean existsById_BukrsAndId_StageIdAndDelstats(int bukrs, int stageid, String delstats);

    StagesM findById_BukrsAndId_StageIdAndDelstats(int bukrs, int stageid, String delstats);

    StagesM findById_BukrsAndDescrAndDelstats(int bukrs, String descr, String delstats);


    @Query("select coalesce( e.status, '') from StagesM e where e.id.bukrs = :bukrs and e.id.stageId =:stageId and e.delstats = ''")
    String findStatus(@Param("bukrs") int bukrs,
                      @Param("stageId") int stageId);

    /*@Query("select e. from StagesM e where e.id.bukrs =:bukrs and e.id.leadId =:leadId and e.stageId =:stageId and e.delstats = ''")
    String findStatus();*/

}