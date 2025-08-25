package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.ForbiddenWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord, Long> {
}
