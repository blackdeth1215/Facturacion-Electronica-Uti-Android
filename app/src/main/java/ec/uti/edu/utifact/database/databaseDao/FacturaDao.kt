package ec.uti.edu.utifact.database.databaseDao

import androidx.room.*
import ec.uti.edu.utifact.database.databadeentity.Facturabd

@Dao
interface FacturaDao {
    @Query("SELECT * FROM factura")
    fun getAll(): List<Facturabd>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(factura: Facturabd): Long

    //obtener la ultima factura insertada
    @Query("SELECT MAX(numero) FROM factura")
    fun getLastId(): Int
}