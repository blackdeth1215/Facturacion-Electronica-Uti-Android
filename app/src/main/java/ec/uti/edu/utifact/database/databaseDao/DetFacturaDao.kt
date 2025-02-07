package ec.uti.edu.utifact.database.databaseDao

import androidx.room.*
import ec.uti.edu.utifact.database.databadeentity.DetFacturabd

@Dao
interface DetFacturaDao {
    @Query("SELECT * FROM detallefactura")
    fun getAll(): List<DetFacturabd>

    @Query("SELECT * FROM detallefactura WHERE id = :id")
    fun findById(id: String): DetFacturabd?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(detalleFactura: DetFacturabd): Long

    @Update
    fun update(detalleFactura: DetFacturabd): Int

    @Delete
    fun delete(detalleFactura: DetFacturabd)
}