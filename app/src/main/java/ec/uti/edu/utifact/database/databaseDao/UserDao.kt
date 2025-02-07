package ec.uti.edu.utifact.database.databaseDao

import androidx.room.*
import ec.uti.edu.utifact.database.databadeentity.Userbd

@Dao
interface UserDao {
    @Query("SELECT * FROM usuarios")
    fun getAll(): List<Userbd>

    @Query("SELECT * FROM usuarios WHERE user = :user")
    fun findByUser(user: String): Userbd?

    @Query("SELECT * FROM usuarios WHERE user = :user AND password = :password")
    fun findByUserAndPassword(user: String, password: String): Userbd?

    @Query("SELECT * FROM usuarios WHERE user = :username AND password = :password LIMIT 1")
    fun validateUsuario(username: String, password: String): Userbd?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: Userbd): Long

    @Update
    fun update(user: Userbd): Int

    @Update
    fun updateUser(user: Userbd)

    @Delete
    fun delete(user: Userbd)
}