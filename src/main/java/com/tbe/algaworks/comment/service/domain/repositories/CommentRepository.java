package com.tbe.algaworks.comment.service.domain.repositories;

import com.tbe.algaworks.comment.service.domain.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
