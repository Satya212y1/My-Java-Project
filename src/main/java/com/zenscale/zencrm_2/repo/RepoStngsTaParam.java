package com.zenscale.zencrm_2.repo;


import com.zenscale.zencrm_2.entity.StngsTaParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoStngsTaParam extends JpaRepository<StngsTaParam, Integer> {


    boolean existsById(int bukrs);
}