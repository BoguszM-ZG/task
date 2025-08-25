package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieNotification;
import com.tcode.moviebase.Entities.UserAlert;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Exceptions.NotificationException;
import com.tcode.moviebase.Repositories.MovieNotificationRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.UserAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MovieNotificationRepository movieNotificationRepository;
    private final MovieRepository movieRepository;
    private final UserAlertRepository userAlertRepository;

    @Transactional
    public void removeNotification(String userId, Long movieId) {
        if (movieNotificationRepository.existsByUserIdAndMovieId(userId, movieId)) {
            movieNotificationRepository.deleteByUserIdAndMovieId(userId, movieId);
        } else {
            throw new NotificationException("Notification doesnt exists");

        }

    }

    public void addNotification(String userId, Long movieId) {
        var movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        if (movieNotificationRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new NotificationException("Notification already exists");
        }
        var notification = new MovieNotification(userId, movie);
        movieNotificationRepository.save(notification);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void fetchNotificationsForWorldPremiere() {

        List<Movie> movies = movieRepository.findMovieByWorld_premiere(LocalDate.now().plusDays(1));
        for (Movie movie : movies) {
            MovieNotification notification = movieNotificationRepository.findByMovieId(movie.getId());
            if (notification != null) {
                String message = "Tomorrow is the world premiere of " + movie.getTitle() + "!";
                UserAlert userAlert = new UserAlert(notification.getUserId(), message);
                userAlert.setMessage(message);
                userAlert.setRead(false);
                userAlertRepository.save(userAlert);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void fetchNotificationsForPolishPremiere() {

        List<Movie> movies = movieRepository.findMovieByPolish_premiere(LocalDate.now().plusDays(1));
        for (Movie movie : movies) {
            MovieNotification notification = movieNotificationRepository.findByMovieId(movie.getId());
            if (notification != null) {
                String message = "Tomorrow is the polish premiere of " + movie.getTitle() + "!";
                UserAlert userAlert = new UserAlert(notification.getUserId(), message);
                userAlert.setMessage(message);
                userAlert.setRead(false);
                userAlertRepository.save(userAlert);
            }
        }
    }
}
