package com.tbe.algaworks.comment.service.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentInput {

    private String text;
    private String author;
}
