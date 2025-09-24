/**
 * 母婴商城公共模块 - 聚合模块
 * 
 * <p>本模块是所有功能子模块的聚合模块，提供向后兼容性支持。</p>
 * 
 * <h2>主要功能</h2>
 * <ul>
 *   <li>聚合所有功能子模块</li>
 *   <li>提供统一的自动配置</li>
 *   <li>向后兼容的包结构映射</li>
 *   <li>简化依赖管理</li>
 * </ul>
 * 
 * <h2>使用方式</h2>
 * 
 * <h3>Maven依赖</h3>
 * <pre>{@code
 * <dependency>
 *     <groupId>com.muyingmall</groupId>
 *     <artifactId>muying-mall-common-all</artifactId>
 *     <version>1.0.0</version>
 * </dependency>
 * }</pre>
 * 
 * <h3>自动配置</h3>
 * <p>引入此模块后，所有功能模块将自动配置，无需额外配置。</p>
 * 
 * <h3>向后兼容</h3>
 * <p>现有代码可以继续使用原有的包路径：</p>
 * <pre>{@code
 * // 原有代码仍然可用（但建议迁移到新包）
 * import com.muyingmall.common.dto.Result;
 * import com.muyingmall.common.exception.BusinessException;
 * import com.muyingmall.common.util.JwtUtils;
 * 
 * // 推荐使用新包路径
 * import com.muyingmall.common.core.result.Result;
 * import com.muyingmall.common.core.exception.BusinessException;
 * import com.muyingmall.common.security.jwt.JwtUtils;
 * }</pre>
 * 
 * <h2>包含的功能模块</h2>
 * <ul>
 *   <li>{@link com.muyingmall.common.core} - 核心基础功能</li>
 *   <li>{@link com.muyingmall.common.redis} - Redis缓存功能</li>
 *   <li>{@link com.muyingmall.common.swagger} - API文档功能</li>
 *   <li>{@link com.muyingmall.common.security} - 安全认证功能</li>
 *   <li>{@link com.muyingmall.common.rabbitmq} - 消息队列功能</li>
 *   <li>{@link com.muyingmall.common.web} - Web相关功能</li>
 * </ul>
 * 
 * <h2>迁移指南</h2>
 * <p>虽然提供了向后兼容性，但建议逐步迁移到新的包结构：</p>
 * <ol>
 *   <li>将 {@code com.muyingmall.common.dto.*} 迁移到 {@code com.muyingmall.common.core.result.*}</li>
 *   <li>将 {@code com.muyingmall.common.exception.*} 迁移到 {@code com.muyingmall.common.core.exception.*}</li>
 *   <li>将 {@code com.muyingmall.common.util.*} 迁移到对应的功能模块</li>
 *   <li>将 {@code com.muyingmall.common.enums.*} 迁移到 {@code com.muyingmall.common.core.enums.*}</li>
 * </ol>
 * 
 * <h2>注意事项</h2>
 * <ul>
 *   <li>向后兼容的类标记为 {@code @Deprecated}，建议尽快迁移</li>
 *   <li>新项目建议直接使用具体的功能模块而非聚合模块</li>
 *   <li>聚合模块主要用于现有项目的平滑迁移</li>
 * </ul>
 * 
 * @author 青柠檬
 * @since 2025-09-24
 * @version 1.0.0
 */
package com.muyingmall.common.all;