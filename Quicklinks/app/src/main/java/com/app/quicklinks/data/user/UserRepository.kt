package com.app.quicklinks.data.user

class UserRepository(private val dao: UserDao) {

    suspend fun createUser(email: String, password: String) {
        val user = UserEntity(email, password)
        dao.insertUser(user)
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        return dao.login(email, password) != null
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

}
