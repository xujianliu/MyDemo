package com.bruce.room.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * 操作数据库的增删改查
 *@author xujian
 *@date 2021/9/25
 */
@Dao //Dao = data access objects包含用于访问数据库的方法。
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    fun findUser(first: String, last: String): LiveData<List<User>>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertUser(user: User):LiveData<Long>

    @Insert
    fun insertUserList(list: List<User>):List<Long>

    @Delete
    fun delete(user: User):Int
}