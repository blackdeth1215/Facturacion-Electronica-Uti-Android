package ec.uti.edu.utifact.entity

import androidx.room.*

@Dao
interface DetalleFactura {
    @Query("SELECT * FROM detalleFactura")
    fun getAll(): List<DetalleFactura>

    @Query("SELECT * FROM detallefactura WHERE id = :id")
    fun findById(id: Int): DetalleFactura?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(detalleFactura: DetalleFactura): Long

    @Update
    fun update(detalleFactura: DetalleFactura): Int

    @Delete
    fun delete(detalleFactura: DetalleFactura)
}