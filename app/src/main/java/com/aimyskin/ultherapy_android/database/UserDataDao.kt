package com.aimyskin.nodeparser.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.pojo.UserWithRecordList
import kotlinx.coroutines.flow.Flow

/**
 * 本地数据库需要用到的 模板Dao
 */
@Dao
interface UserDataDao {
    /**
     * 分页查看User
     */
    @Transaction
    @Query("SELECT * FROM user ORDER BY user_id DESC LIMIT :limit OFFSET :offset")
    fun getUserList(limit: Int, offset: Int): Flow<List<User>>

    /**
     * 添加一个User
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    /**
     * 删除一个或多个User
     */
    @Delete
    fun deleteUser(vararg user: User)

    /**
     * 通过电话号码搜索User列表
     */
    @Transaction
    @Query("SELECT * FROM user WHERE telephone LIKE :telephone ORDER BY user_id DESC LIMIT 20 ")
    fun searchUserListFromLocalByTelephone(telephone: String): Flow<List<User>>

}