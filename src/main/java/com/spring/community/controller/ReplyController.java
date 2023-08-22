package com.spring.community.controller;

import com.spring.community.entity.Reply;
import com.spring.community.exception.NotFoundReplyByReplyIdException;
import com.spring.community.service.ReplyService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<Reply>> getAllRepliesByPostId (@PathVariable long postId) {
        List<Reply> replies = replyService.findAllByPostId(postId);

        return ResponseEntity
                .ok()
                .body(replies);
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<?> getReplyByReplyId (@PathVariable long replyId) {
        Reply reply = replyService.findByReplyId(replyId);
        if (reply == null) {
            try {
                throw new NotFoundReplyByReplyIdException("존재하지 않는 댓글입니다.");
            } catch (NotFoundReplyByReplyIdException e) {
                e.printStackTrace();
                return new ResponseEntity<>("댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.ok(reply);
    }

    @PostMapping
    public ResponseEntity<String> createReply(@RequestBody Reply reply) {
        replyService.save(reply);

        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId) {
        try {
            replyService.deleteByReplyId(replyId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundReplyByReplyIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{replyId}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<String> updateReply(@PathVariable long replyId, @RequestBody Reply reply) {
        try {
            reply.setReplyId(replyId);
            replyService.update(reply);

            return ResponseEntity.ok("댓글이 수정되었습니다.");
        } catch (NotFoundReplyByReplyIdException e) {
            return ResponseEntity.notFound().build();
        }
    }
}