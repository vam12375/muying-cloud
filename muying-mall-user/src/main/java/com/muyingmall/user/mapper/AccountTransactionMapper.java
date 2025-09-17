package com.muyingmall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.user.dto.TransactionQueryDTO;
import com.muyingmall.user.entity.AccountTransaction;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface AccountTransactionMapper extends BaseMapper<AccountTransaction> {

    Logger log = LoggerFactory.getLogger(AccountTransactionMapper.class);

    @Select({
        "<script>",
        "SELECT",
        "  t.transaction_id as id, t.user_id, t.account_id, t.type, t.amount, t.balance, t.status,",
        "  t.payment_method, t.transaction_no, t.related_id as relatedId, t.create_time, t.update_time, t.description, t.remark,",
        "  u.user_id as u_user_id, u.username as u_username, u.nickname as u_nickname, u.email as u_email, u.phone as u_phone",
        "FROM",
        "  account_transaction t",
        "LEFT JOIN",
        "  user u ON t.user_id = u.user_id",
        "<where>",
        "  <if test='userId != null'> AND t.user_id = #{userId} </if>",
        "  <if test='type != null'> AND t.type = #{type} </if>",
        "  <if test='status != null'> AND t.status = #{status} </if>",
        "  <if test='paymentMethod != null and paymentMethod != \"\"'> AND t.payment_method = #{paymentMethod} </if>",
        "  <if test='transactionNo != null and transactionNo != \"\"'> AND t.transaction_no = #{transactionNo} </if>",
        "  <if test='startTime != null'> AND t.create_time &gt;= #{startTime} </if>",
        "  <if test='endTime != null'> AND t.create_time &lt;= #{endTime} </if>",
        "  <if test='keyword != null and keyword != \"\"'>",
        "    AND (u.username LIKE CONCAT('%', #{keyword}, '%')",
        "    OR u.nickname LIKE CONCAT('%', #{keyword}, '%')",
        "    OR u.email LIKE CONCAT('%', #{keyword}, '%')",
        "    OR u.phone LIKE CONCAT('%', #{keyword}, '%'))",
        "  </if>",
        "</where>",
        "ORDER BY t.create_time DESC",
        "</script>"
    })
    @Results({
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "account_id", property = "accountId"),
        @Result(property = "user", column = "user_id", javaType = com.muyingmall.user.entity.User.class, one = @One(select = "com.muyingmall.user.mapper.UserMapper.selectById"))
    })
    List<AccountTransaction> selectTransactionList(TransactionQueryDTO queryDTO);

    @Select("SELECT transaction_id as id, user_id, account_id, type, amount, balance, status, payment_method, " +
            "transaction_no, related_id as relatedId, create_time, update_time, description, remark " +
            "FROM account_transaction WHERE transaction_id = #{id}")
    AccountTransaction selectById(@Param("id") Integer id);

    @Insert("INSERT INTO account_transaction (user_id, account_id, type, amount, balance, status, " +
            "payment_method, transaction_no, related_id, create_time, update_time, description, remark) " +
            "VALUES (#{userId}, #{accountId}, #{type}, #{amount}, #{balance}, #{status}, " +
            "#{paymentMethod}, #{transactionNo}, #{relatedId}, #{createTime}, #{updateTime}, #{description}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "transaction_id")
    int insert(AccountTransaction transaction);

    default void secureInsert(AccountTransaction transaction) {
        log.debug("Preparing to insert transaction: {}", transaction);
        if (transaction.getAccountId() == null || transaction.getAccountId() <= 0) {
            log.error("Validation failed: Account ID is null or invalid. Transaction: {}", transaction);
            throw new IllegalArgumentException("Transaction accountId cannot be null or invalid: " + transaction.getAccountId());
        }
        if (transaction.getUserId() == null || transaction.getUserId() <= 0) {
            log.error("Validation failed: User ID is null or invalid. Transaction: {}", transaction);
            throw new IllegalArgumentException("Transaction userId cannot be null or invalid: " + transaction.getUserId());
        }
        log.debug("Transaction validated, proceeding with insert: {}", transaction);
        insert(transaction);
        log.debug("Transaction inserted successfully with ID: {}", transaction.getId());
    }

    @Update("UPDATE account_transaction SET status = #{status}, update_time = #{updateTime} WHERE transaction_id = #{id}")
    int updateStatus(AccountTransaction transaction);

    @Select("SELECT transaction_id as id, user_id, account_id, type, amount, balance, status, payment_method, " +
            "transaction_no, related_id as relatedId, create_time, update_time, description, remark " +
            "FROM account_transaction WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<AccountTransaction> selectByUserId(@Param("userId") Integer userId);

    IPage<AccountTransaction> getTransactionPage(Page<AccountTransaction> page, @Param("query") TransactionQueryDTO queryDTO);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM account_transaction WHERE user_id = #{userId} AND type = #{type} AND status = 1")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") Integer userId, @Param("type") Integer type);

    @Select("SELECT type, status, COUNT(*) as count, SUM(amount) as total_amount FROM account_transaction WHERE user_id = #{userId} GROUP BY type, status")
    List<Map<String, Object>> getTransactionStatsByUserId(@Param("userId") Integer userId);
}
