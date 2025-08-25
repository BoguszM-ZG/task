package com.tcode.moviebase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "movie_trailer", schema = "movies")
public class MovieTrailer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "video_data", nullable = false)
    private byte[] videoData;

    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;

    @Size(max = 100)
    @Column(name = "content_type", length = 100)
    private String contentType;



    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

}