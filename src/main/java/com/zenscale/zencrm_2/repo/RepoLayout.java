package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.LayoutIdentity;
import com.zenscale.zencrm_2.entity.layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoLayout extends JpaRepository<layout, LayoutIdentity> {

}