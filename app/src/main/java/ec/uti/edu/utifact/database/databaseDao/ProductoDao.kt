package ec.uti.edu.utifact.database.databaseDao

import androidx.room.*
import ec.uti.edu.utifact.database.databadeentity.Productobd

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    fun getAll(): List<Productobd>

    @Query("SELECT * FROM productos WHERE codeProduct = :codigo")
    fun findByCode(codigo: String): Productobd?

    @Query("SELECT * FROM productos WHERE codeProduct = :codigo")
    fun findByCodeList(codigo: String):  List<Productobd>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(producto: Productobd): Long

    @Update
    fun update(producto: Productobd): Int

    @Delete
    fun delete(producto: Productobd)

}