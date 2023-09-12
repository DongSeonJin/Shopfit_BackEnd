package com.spring.community.controller;

import com.spring.community.DTO.ReplyResponseDTO;
import com.spring.community.DTO.ReplyUpdateRequestDTO;
import com.spring.community.entity.Reply;
import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.community.exception.NotFoundReplyByReplyIdException;
import com.spring.community.service.PostService;
import com.spring.community.service.ReplyService;
import com.spring.user.entity.User;
import com.spring.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService, PostService postService, UserService userService) {
        this.replyService = replyService;
    }

    // postId 에 해당하는 댓글 전부 가져오기
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

    // replyId 에 해당하는 댓글 가져오기
    @GetMapping("/{replyId}")
    public ResponseEntity<?> getReplyByReplyId (@PathVariable long replyId) {
        Reply reply = replyService.findByReplyId(replyId);

        // * 서비스 레이어에서 이미 예외처리를 해주므로 예외처리 할필요X *

//        if (reply == null) {
//            try {
//                throw new NotFoundReplyByReplyIdException("존재하지 않는 댓글입니다.");
//            } catch (NotFoundReplyByReplyIdException e) {
//                e.printStackTrace();
//                return new ResponseEntity<>("댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
//            }
//        }

        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO(reply);

        return ResponseEntity.ok(replyResponseDTO);

    }
  

    // 댓글 작성하기
    @PostMapping
    public ResponseEntity<String> createReply(@RequestBody Reply reply) {

        replyService.save(reply);

        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 등록 성공");
//        try {
//            Reply reply = Reply.builder()
//                    .content(replyCreateRequestDTO.getContent())
//                    .build();
//
//            User user = User.builder()
//                    .nickname(replyCreateRequestDTO.getNickname())
//                    .build();
//
//            reply.setUser(user);
//
//            replyService.save(reply);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body("댓글 등록 성공");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 등록 실패");
//        }

    }


    // 댓글 삭제하기
    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId) {
        try {
            replyService.deleteByReplyId(replyId);

            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (NotFoundReplyByReplyIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 댓글 수정하기
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

