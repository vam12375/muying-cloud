package com.muyingmall.comment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.comment.dto.CommentDTO;
import com.muyingmall.comment.dto.CommentStatsDTO;
import com.muyingmall.comment.dto.CommentWithTagsDTO;
import com.muyingmall.comment.entity.Comment;
import com.muyingmall.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器
 */
@Tag(name = "评论管理", description = "商品评论管理相关接口")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "创建评论")
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.createComment(commentDTO);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "根据ID获取评论")
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(
            @Parameter(description = "评论ID") @PathVariable Long id) {
        Comment comment = commentService.getById(id);
        return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "分页查询评论")
    @GetMapping("/page")
    public ResponseEntity<IPage<CommentWithTagsDTO>> getCommentsPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long productId,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "评论状态") @RequestParam(required = false) String status,
            @Parameter(description = "评分") @RequestParam(required = false) Integer rating) {
        
        Page<CommentWithTagsDTO> page = new Page<>(current, size);
        IPage<CommentWithTagsDTO> result = commentService.getCommentsPage(page, productId, userId, status, rating);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取商品评论列表")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentWithTagsDTO>> getCommentsByProductId(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "time") String sortBy,
            @Parameter(description = "评分筛选") @RequestParam(required = false) Integer rating,
            @Parameter(description = "是否有图") @RequestParam(required = false) Boolean hasImages) {
        
        List<CommentWithTagsDTO> comments = commentService.getCommentsByProductId(productId, sortBy, rating, hasImages);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "获取用户评论列表")
    @GetMapping("/user/{userId}")
    public ResponseEntity<IPage<CommentWithTagsDTO>> getCommentsByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Page<CommentWithTagsDTO> page = new Page<>(current, size);
        IPage<CommentWithTagsDTO> result = commentService.getCommentsByUserId(page, userId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取商品评论统计")
    @GetMapping("/stats/product/{productId}")
    public ResponseEntity<CommentStatsDTO> getCommentStats(
            @Parameter(description = "商品ID") @PathVariable Long productId) {
        CommentStatsDTO stats = commentService.getCommentStats(productId);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "更新评论")
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @Parameter(description = "评论ID") @PathVariable Long id,
            @RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "评论ID") @PathVariable Long id) {
        commentService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "审核评论")
    @PutMapping("/{id}/audit")
    public ResponseEntity<Comment> auditComment(
            @Parameter(description = "评论ID") @PathVariable Long id,
            @Parameter(description = "审核状态") @RequestParam String status,
            @Parameter(description = "审核备注") @RequestParam(required = false) String auditRemark) {
        Comment comment = commentService.auditComment(id, status, auditRemark);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "点赞评论")
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(
            @Parameter(description = "评论ID") @PathVariable Long id,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        commentService.likeComment(id, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "取消点赞评论")
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeComment(
            @Parameter(description = "评论ID") @PathVariable Long id,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        commentService.unlikeComment(id, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "批量审核评论")
    @PutMapping("/audit/batch")
    public ResponseEntity<Void> auditCommentsBatch(
            @Parameter(description = "评论ID列表") @RequestBody List<Long> commentIds,
            @Parameter(description = "审核状态") @RequestParam String status,
            @Parameter(description = "审核备注") @RequestParam(required = false) String auditRemark) {
        commentService.auditCommentsBatch(commentIds, status, auditRemark);
        return ResponseEntity.ok().build();
    }
}