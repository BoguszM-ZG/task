package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.UserAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAlertRepository extends JpaRepository<UserAlert, Integer> {
}
