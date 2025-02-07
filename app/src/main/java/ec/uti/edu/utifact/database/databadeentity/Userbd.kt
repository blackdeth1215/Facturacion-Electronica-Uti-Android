package ec.uti.edu.utifact.database.databadeentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Userbd(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user: String,
    val password: String,
    val fechaCreacion: String,
    val fechaCaducidad: String,
    val rol: Int //rol para seleccionar el rol
)