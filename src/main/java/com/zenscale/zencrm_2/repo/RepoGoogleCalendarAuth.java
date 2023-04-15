package com.zenscale.zencrm_2.repo;

import com.zenscale.zencrm_2.entity.GoogleCalendarAuth;
import com.zenscale.zencrm_2.entity.GoogleCalendarAuthId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoGoogleCalendarAuth extends JpaRepository<GoogleCalendarAuth, GoogleCalendarAuthId> {




}