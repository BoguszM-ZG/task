package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieNotification;
import com.tcode.moviebase.Entities.UserAlert;
import com.tcode.moviebase.Repositories.MovieNotificationRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.UserAlertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private MovieNotificationRepository movieNotificationRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private UserAlertRepository userAlertRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testExistsNotification() {
        when(movieNotificationRepository.existsByUserIdAndMovieId("user", 1L)).thenReturn(true);
        assertTrue(notificationService.existsNotification("user", 1L));
    }

    @Test
    void testRemoveNotification() {
        notificationService.removeNotification("user", 2L);
        verify(movieNotificationRepository).deleteByUserIdAndMovieId("user", 2L);
    }

    @Test
    void testAddNotification() {
        Movie movie = new Movie();
        movie.setId(3L);
        when(movieRepository.findById(3L)).thenReturn(Optional.of(movie));

        notificationService.addNotification("user", 3L);

        ArgumentCaptor<MovieNotification> captor = ArgumentCaptor.forClass(MovieNotification.class);
        verify(movieNotificationRepository).save(captor.capture());
        assertEquals("user", captor.getValue().getUserId());
        assertEquals(movie, captor.getValue().getMovie());
    }

    @Test
    void testFetchNotificationsForWorldPremier() {
        Movie movie = new Movie();
        movie.setId(4L);
        movie.setTitle("Test Movie");
        when(movieRepository.findMovieByWorld_premiere(any(LocalDate.class))).thenReturn(List.of(movie));
        MovieNotification notification = new MovieNotification("user", movie);
        when(movieNotificationRepository.findByMovieId(4L)).thenReturn(notification);

        notificationService.fetchNotificationsForWorldPremiere();

        ArgumentCaptor<UserAlert> captor = ArgumentCaptor.forClass(UserAlert.class);
        verify(userAlertRepository).save(captor.capture());
        assertEquals("user", captor.getValue().getUserId());
        assertTrue(captor.getValue().getMessage().contains("Tomorrow is the world premiere"));
        assertFalse(captor.getValue().getRead());
    }

    @Test
    void testFetchNotificationsForPolishPremiere() {
        Movie movie = new Movie();
        movie.setId(5L);
        movie.setTitle("Polish Movie");
        when(movieRepository.findMovieByPolish_premiere(any(LocalDate.class))).thenReturn(List.of(movie));
        MovieNotification notification = new MovieNotification("user2", movie);
        when(movieNotificationRepository.findByMovieId(5L)).thenReturn(notification);

        notificationService.fetchNotificationsForPolishPremiere();

        ArgumentCaptor<UserAlert> captor = ArgumentCaptor.forClass(UserAlert.class);
        verify(userAlertRepository).save(captor.capture());
        assertEquals("user2", captor.getValue().getUserId());
        assertTrue(captor.getValue().getMessage().contains("Tomorrow is the polish premiere"));
        assertFalse(captor.getValue().getRead());
    }
}