package com.tcode.moviebase.Repositories;


import com.tcode.moviebase.Entities.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DirectorRepository extends JpaRepository<Director, Long> {


    Page<Director> getDirectorsByGender(String gender, Pageable pageable);


    Page<Director> getDirectorByLastName(String lastName, Pageable pageable);

    Page<Director> getDirectorByFirstName(String firstName, Pageable pageable);

    @Query(" select d from Director d order by d.lastName asc")
    Page<Director> getDirectorsOrderByLastNameAsc(Pageable pageable);

    @Query(" select d from Director d order by d.lastName desc")
    Page<Director> getDirectorsOrderByLastNameDesc(Pageable pageable);


}
