package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.SourceM;
import com.zenscale.zencrm_2.entity.SourceMId;
import com.zenscale.zencrm_2.entity.StagesM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RepoSourceM extends JpaRepository<SourceM, SourceMId> {

    @Query("select coalesce(max (e.id.sourceId), 0) from SourceM e where e.id.bukrs = :bukrs")
    int getMaxSourceId(@Param("bukrs") int bukrs);

    @Transactional
    @Modifying
    @Query("update SourceM e set e.delstats = 'X' where e.id.bukrs = :bukrs and e.id.sourceId = :sourceId")
    void updateDelStats(@Param("bukrs")  Integer bukrs,
                        @Param("sourceId")  Integer sourceId);

    boolean existsById_BukrsAndId_SourceIdAndDelstats(int bukrs, int sourceId, String delstats);

    SourceM findById_BukrsAndId_SourceIdAndDelstats(int bukrs, int sourceId, String delstats);

    SourceM findById_BukrsAndDescrAndDelstats(int bukrs, String descr, String delstats);

}