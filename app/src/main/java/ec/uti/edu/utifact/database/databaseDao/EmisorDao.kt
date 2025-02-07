package ec.uti.edu.utifact.database.databaseDao

import androidx.room.*
import ec.uti.edu.utifact.database.databadeentity.Emisorbd

@Dao
interface EmisorDao {
    @Query("SELECT * FROM emisor")
    fun getAll(): List<Emisorbd>

    @Query("SELECT * FROM emisor WHERE id = :id")
    fun findById(id: Int): Emisorbd?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(emisor: Emisorbd): Long

    @Update
    fun update(emisor: Emisorbd): Int

    @Delete
    fun delete(emisor: Emisorbd)

}