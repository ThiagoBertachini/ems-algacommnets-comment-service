package com.tbe.algaworks.comment.service.api.client;

import com.tbe.algaworks.comment.service.api.client.model.ModerationInput;
import com.tbe.algaworks.comment.service.api.client.model.ModerationOutput;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/moderate")
public interface ModerationServiceClient {

    @PostExchange
    ModerationOutput moderateComment(@RequestBody ModerationInput moderationInput);
}
