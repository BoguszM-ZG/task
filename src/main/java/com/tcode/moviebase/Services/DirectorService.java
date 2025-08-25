package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.DirectorDto;
import com.tcode.moviebase.Entities.Director;
import com.tcode.moviebase.Entities.DirectorGrade;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.DirectorNotFoundException;
import com.tcode.moviebase.Exceptions.InvalidDirectorDataException;
import com.tcode.moviebase.Exceptions.MovieAlreadyAssignedToDirectorException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Repositories.DirectorRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private  final MovieRepository movieRepository;

    public DirectorDto addDirector(Director director) {
        if (director.getFirstName() == null || director.getFirstName().isBlank()) {
            throw new InvalidDirectorDataException("First name is required");
        }
        if (director.getLastName() == null || director.getLastName().isBlank()) {
            throw new InvalidDirectorDataException("Last name is required");
        }
        if (director.getDateOfBirth() == null) {
            throw new InvalidDirectorDataException("Date of birth is required");
        }
        directorRepository.save(director);
        return directorToDirectorDto(director);
    }

    @Transactional
    public void addMovieToDirector(Long directorId, Long movieId) {
        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found "));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found)"));

        if (director.getMovies().contains(movie)) {
            throw new MovieAlreadyAssignedToDirectorException("Movie already assigned to director");
        }

        director.getMovies().add(movie);
        movie.getDirectors().add(director);

        directorRepository.save(director);
        movieRepository.save(movie);
    }



    public DirectorDto getDirectorById(Long id){
        return directorRepository.findById(id).map(this::directorToDirectorDto).orElseThrow(() -> new DirectorNotFoundException("Director not found"));
    }


    public void deleteDirector(Long id) {
        if (directorRepository.existsById(id))
        {
            directorRepository.deleteById(id);
        }else
        {
            throw new DirectorNotFoundException("Director not found");
        }
    }

    public Page<DirectorDto> getDirectorsDtoByGender(String gender, Pageable pageable) {
        return directorRepository.getDirectorsByGender(gender, pageable).map(this::directorToDirectorDto);
    }


    public Page<DirectorDto> getDirectorsDtoSortedByLastNameAsc(Pageable pageable) {
        return directorRepository.getDirectorsOrderByLastNameAsc(pageable).map(this::directorToDirectorDto);
    }

    public Page<DirectorDto> getDirectorsDtoSortedByLastNameDesc(Pageable pageable) {
        return directorRepository.getDirectorsOrderByLastNameDesc(pageable).map(this::directorToDirectorDto);
    }

    public Page<DirectorDto> getDirectorsByFirstName(String firstName, Pageable pageable) {
        return directorRepository.getDirectorByFirstName(firstName, pageable).map(this::directorToDirectorDto);
    }

    public Page<DirectorDto> getDirectorsByLastName(String lastName, Pageable pageable) {
        return directorRepository.getDirectorByLastName(lastName, pageable).map(this::directorToDirectorDto);
    }

    public Page<DirectorDto> getAllDirectors(Pageable pageable) {
        return directorRepository.findAll(pageable).map(this::directorToDirectorDto);
    }

    private DirectorDto directorToDirectorDto(Director director) {
        DirectorDto directorDto = new DirectorDto();
        directorDto.setFirstName(director.getFirstName());
        directorDto.setLastName(director.getLastName());
        directorDto.setBirthDate(director.getDateOfBirth());
        directorDto.setDeathDate(director.getDateOfDeath());
        directorDto.setBiography(director.getBiography());
        directorDto.setGender(director.getGender());


        directorDto.setRating(director.getDirectorGrades().stream()
                .mapToDouble(DirectorGrade::getGrade)
                .average()
                .orElse(0.0)
        );

        directorDto.setMovieTitles(director.getMovies().stream()
            .map(Movie::getTitle).toList());
        return directorDto;
    }

}
