package com.tbe.algaworks.comment.service.api.client.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModerationOutput {
    private String reason;
    private boolean approved;
}
