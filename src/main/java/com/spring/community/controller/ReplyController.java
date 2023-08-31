package com.spring.community.controller;

import com.spring.community.DTO.ReplyCreateRequestDTO;
import com.spring.community.DTO.ReplyResponseDTO;
import com.spring.community.DTO.ReplyUpdateRequestDTO;
import com.spring.community.entity.Reply;
import com.spring.community.exception.NotFoundReplyByReplyIdException;
import com.spring.community.service.ReplyService;
import com.spring.user.entity.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<ReplyResponseDTO>> getAllRepliesByPostId (@PathVariable long postId) {
        List<Reply> replies = replyService.findAllByPostId(postId);

        List<ReplyResponseDTO> replyResponseDTOS = new ArrayList<>();
        for (Reply reply : replies) {
            ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO(reply);
            replyResponseDTOS.add(replyResponseDTO);
        }

        return ResponseEntity
                .ok()
                .body(replyResponseDTOS);
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
        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO(reply);
        return ResponseEntity.ok(replyResponseDTO);
    }

    @PostMapping("")
    public ResponseEntity<String> createReply(@RequestBody Reply reply) {
        replyService.save(reply);

        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId) {
        try {
            replyService.deleteByReplyId(replyId);

            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (NotFoundReplyByReplyIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{replyId}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<String> updateReply(@PathVariable long replyId, @RequestBody ReplyUpdateRequestDTO replyUpdateRequestDTO) {
        try {
            Reply reply = Reply.builder()
                    .replyId(replyId)
                    .content(replyUpdateRequestDTO.getContent())
                    .updatedAt(replyUpdateRequestDTO.getUpdatedAt())
                    .build();

            User user = User.builder()
                    .nickname(replyUpdateRequestDTO.getNickname())
                    .build();

            reply.setUser(user);
            replyService.update(reply);

            return ResponseEntity.ok("댓글이 수정되었습니다.");
        } catch (NotFoundReplyByReplyIdException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
