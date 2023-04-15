package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.StngLeadAssignment;
import com.zenscale.zencrm_2.entity.StngLeadAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoStngLeadAssignment extends JpaRepository<StngLeadAssignment, StngLeadAssignmentId> {

}