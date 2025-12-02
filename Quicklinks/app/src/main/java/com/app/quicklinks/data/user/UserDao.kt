package com.app.quicklinks.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("SELECT * FROM users WHERE email = :userEmail LIMIT 1")
    suspend fun getUserByEmail(userEmail: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :userName LIMIT 1")
    suspend fun getUserByUsername(userName: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :emailOrUsername AND password = :password OR username = :emailOrUsername AND password = :password LIMIT 1")
    suspend fun login(emailOrUsername: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginByUsername(username: String, password: String): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET username = :newUsername WHERE id = :userId")
    suspend fun updateUsername(newUsername: String, userId: Long)

    @Query("UPDATE users SET email = :newEmail WHERE id = :userId")
    suspend fun updateEmail(newEmail: String, userId: Long)

    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
    suspend fun updatePassword(newPassword: String, userId: Long)


}
