package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.StngIdentity;
import com.zenscale.zencrm_2.entity.stngs_bukrs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoStng extends JpaRepository<stngs_bukrs, StngIdentity> {

}
