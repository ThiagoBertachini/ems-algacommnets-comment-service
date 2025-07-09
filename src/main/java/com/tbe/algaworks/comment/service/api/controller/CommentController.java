package com.tbe.algaworks.comment.service.api.controller;

import com.tbe.algaworks.comment.service.api.client.ModerationServiceClient;
import com.tbe.algaworks.comment.service.api.client.exceptions.UnprocessableEntityException;
import com.tbe.algaworks.comment.service.api.client.model.ModerationInput;
import com.tbe.algaworks.comment.service.api.client.model.ModerationOutput;
import com.tbe.algaworks.comment.service.api.model.CommentInput;
import com.tbe.algaworks.comment.service.api.model.CommentOutput;
import com.tbe.algaworks.comment.service.domain.models.Comment;
import com.tbe.algaworks.comment.service.domain.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final ModerationServiceClient moderationServiceClient;
    private final CommentRepository commentRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutput createComment(@RequestBody CommentInput commentInput) {
        log.info("Sending comment to moderation service: '{}'", commentInput.getText());
        ModerationInput moderationInput = ModerationInput.builder()
                .commentId(UUID.randomUUID())
                .text(commentInput.getText())
                .build();

        ModerationOutput moderationOutput =
                moderationServiceClient.moderateComment(moderationInput);
        if (!moderationOutput.isApproved()) {
            throw new UnprocessableEntityException(moderationOutput.getReason());
        }

        Comment comment = commentRepository.save(createEntity(commentInput, moderationInput));
        return createCommentResponse(comment);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentOutput getComment(@PathVariable("id") UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return createCommentResponse(comment);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentOutput> getComments(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(this::createCommentResponse);
    }

    private CommentOutput createCommentResponse(Comment comment) {
        return CommentOutput.builder()
                .id(comment.getId())
                .author(comment.getAuthor())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private Comment createEntity(CommentInput commentInput, ModerationInput moderationInput) {
        return Comment.builder()
                .id(moderationInput.getCommentId())
                .author(commentInput.getAuthor())
                .text(commentInput.getText())
                .createdAt(OffsetDateTime.now()).build();
    }
}
