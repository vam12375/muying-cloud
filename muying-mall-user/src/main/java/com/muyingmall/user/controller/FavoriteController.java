package com.muyingmall.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.common.dto.Result;
import com.muyingmall.user.entity.Favorite;
import com.muyingmall.user.entity.User;
import com.muyingmall.user.service.FavoriteService;
import com.muyingmall.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/user/favorites")
@RequiredArgsConstructor
@Tag(name = "收藏管理", description = "用户收藏商品的相关接口")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    /**
     * 获取收藏列表
     */
    @GetMapping
    @Operation(summary = "获取收藏列表")
    public Result<Page<Favorite>> getFavorites(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Result.error(401, "用户未认证");
        }

        // 从SecurityContext中获取用户名
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        Page<Favorite> favorites = favoriteService.getUserFavorites(user.getUserId(), page, pageSize);
        return Result.success(favorites);
    }

    /**
     * 添加收藏
     */
    @PostMapping
    @Operation(summary = "添加收藏")
    public Result<Favorite> addFavorite(@RequestParam Integer productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Result.error(401, "用户未认证");
        }

        // 从SecurityContext中获取用户名
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        try {
            Favorite favorite = favoriteService.addFavorite(user.getUserId(), productId);
            return Result.success("收藏成功", favorite);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 移除收藏
     */
    @DeleteMapping("/{favoriteId}")
    @Operation(summary = "移除收藏")
    public Result<Void> removeFavorite(@PathVariable Integer favoriteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Result.error(401, "用户未认证");
        }

        try {
            boolean success = favoriteService.removeFavorite(favoriteId);
            if (success) {
                return Result.success("移除成功");
            } else {
                return Result.error("移除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 清空收藏夹
     */
    @DeleteMapping("/clear")
    @Operation(summary = "清空收藏夹")
    public Result<Void> clearFavorites() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Result.error(401, "用户未认证");
        }

        // 从SecurityContext中获取用户名
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        try {
            boolean success = favoriteService.clearFavorites(user.getUserId());
            if (success) {
                return Result.success("清空成功");
            } else {
                return Result.error("清空失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}