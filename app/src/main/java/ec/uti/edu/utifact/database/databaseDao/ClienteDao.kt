package ec.uti.edu.utifact.database.databaseDao

import androidx.room.*
import ec.uti.edu.utifact.database.databadeentity.Clientebd

@Dao
interface ClienteDao {
    @Query("SELECT * FROM clientes")
    fun getAll(): List<Clientebd>

    @Query("SELECT * FROM clientes WHERE cedulaClient = :cedula")
    fun findByCedula(cedula: String): Clientebd?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cliente: Clientebd): Long

    @Update
    fun update(cliente: Clientebd): Int

    @Delete
    fun delete(cliente: Clientebd)
}