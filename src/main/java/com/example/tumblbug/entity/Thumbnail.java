package com.example.tumblbug.entity;

import com.example.tumblbug.dto.ImageRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "thumbnail")
public class Thumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thumbnailId;

    private String url;

    private String filename;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    public Thumbnail(ImageRequestDto imageRequestDto, Project project) {
        this.url = imageRequestDto.getUrl();
        this.filename = imageRequestDto.getFilename();
        this.project = project;
    }

}
