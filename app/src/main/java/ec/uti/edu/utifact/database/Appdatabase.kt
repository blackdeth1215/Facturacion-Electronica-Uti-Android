package ec.uti.edu.utifact.database

import android.content.Context
import androidx.room.*
import ec.uti.edu.utifact.database.databadeentity.Clientebd
import ec.uti.edu.utifact.database.databadeentity.DetFacturabd
import ec.uti.edu.utifact.database.databadeentity.Emisorbd
import ec.uti.edu.utifact.database.databadeentity.Facturabd
import ec.uti.edu.utifact.database.databadeentity.Productobd
import ec.uti.edu.utifact.database.databadeentity.Userbd
import ec.uti.edu.utifact.database.databaseDao.ClienteDao
import ec.uti.edu.utifact.database.databaseDao.DetFacturaDao
import ec.uti.edu.utifact.database.databaseDao.EmisorDao
import ec.uti.edu.utifact.database.databaseDao.FacturaDao
import ec.uti.edu.utifact.database.databaseDao.ProductoDao
import ec.uti.edu.utifact.database.databaseDao.UserDao

@Database(
    entities = [Userbd::class, Clientebd::class, Emisorbd::class, Productobd::class, Facturabd::class, DetFacturabd::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun clienteDao(): ClienteDao
    abstract fun emisorDao(): EmisorDao
    abstract fun productoDao(): ProductoDao
    abstract fun facturaDao(): FacturaDao
    abstract fun detalleFacturaDao(): DetFacturaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "base_database_facturacion.bd"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}