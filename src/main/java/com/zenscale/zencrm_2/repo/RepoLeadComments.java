package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.LeadComments;
import com.zenscale.zencrm_2.entity.LeadCommentsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoLeadComments extends JpaRepository<LeadComments, LeadCommentsId> {


    @Query("select coalesce(max (e.id.commentId), 0) from LeadComments e where e.id.bukrs = :bukrs")
    Integer getMaxCommentId(@Param("bukrs") int bukrs);



}