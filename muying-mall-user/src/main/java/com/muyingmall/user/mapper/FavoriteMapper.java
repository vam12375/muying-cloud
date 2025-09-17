package com.muyingmall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.user.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper接口
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}