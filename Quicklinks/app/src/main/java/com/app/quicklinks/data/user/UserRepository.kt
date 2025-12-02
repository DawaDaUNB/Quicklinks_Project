package com.app.quicklinks.data.user

import com.app.quicklinks.data.Scan

class UserRepository(private val dao: UserDao) {

    suspend fun createUser(email: String, username: String, password: String) {
        dao.insertUser(UserEntity(
            username = username,
            email = email,
            password = password
        ))
    }

    suspend fun loginUser(emailOrUsername: String, password: String): Boolean {
        return dao.login(emailOrUsername, password) != null
    }

    suspend fun doesUserExist(userId: Long): Boolean {
        return dao.getUserById(userId) != null
    }
    suspend fun doesUserExist(email: String): Boolean {
        return dao.getUserByEmail(email) != null
    }

    suspend fun updatePassword(email: String, newPassword: String): Boolean {
        val user = dao.getUserByEmail(email)
        return if (user != null) {
            val updatedUser = user.copy(password = newPassword)
            dao.updateUser(updatedUser)
            true
        } else {
            false
        }
    }

    suspend fun updateEmail(userId: Long, newEmail: String): Boolean {
        val user = dao.getUserById(userId)
        if (user != null) {
            dao.updateEmail(newEmail, userId)
            return true
        }
        return false
    }

    suspend fun updatePassword(userId: Long, newPassword: String): Boolean {
        val user = dao.getUserById(userId)
        if (user != null) {
            dao.updatePassword(newPassword, userId)
            return true
        }
        return false
    }

    suspend fun updateUsername(userId: Long, newUsername: String): Boolean {
        val user = dao.getUserById(userId)
        if (user != null) {
            dao.updateUsername(newUsername, userId)
            return true
        }
        return false
    }
}
