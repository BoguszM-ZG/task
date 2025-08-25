package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.ForumNameDto;
import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Exceptions.ForumNotFoundException;
import com.tcode.moviebase.Exceptions.InvalidForumDataException;
import com.tcode.moviebase.Repositories.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForumService {
    private  final ForumRepository forumRepository;

    public ForumNameDto addForum(Forum forum) {
        if (forum.getForumName() == null || forum.getForumName().isEmpty()) {
            throw new InvalidForumDataException("Forum name is empty");
        }
        else {
            forumRepository.save(forum);
            return forumToDto(forum);
        }
    }

    public Forum getForumById(Long id) {
        return forumRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteForum(Long id) {
        if (forumRepository.existsById(id)) {
            forumRepository.deleteById(id);
        }
        else {
            throw new ForumNotFoundException("Forum not found");
        }
    }

    public Page<ForumNameDto> getAllForums(Pageable pageable) {
        return forumRepository.findAll(pageable).map(this::forumToDto);
    }


    private ForumNameDto forumToDto(Forum forum) {
        return new ForumNameDto(forum.getForumName());
    }

    public ForumNameDto getForumNameById(Long id) {
        var forum = forumRepository.findById(id).orElseThrow( () -> new ForumNotFoundException("Forum not found"));
        return forumToDto(forum);
    }


}
