package com.example.laboralkutxatpv

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

// Define las entidades y la versión de la base de datos
@Database(entities = [ProductoCompleto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Declara los DAOs
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val dbFile = context.getDatabasePath("TPV.db")

                val builder = if (dbFile.exists()) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "TPV.db"
                    )
                } else {
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "TPV.db"
                    ).createFromAsset("TPV.db")
                }

                val instance = builder
                    .fallbackToDestructiveMigration() // Opcional: cuidado con este
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // Función para copiar la base de datos desde los assets si no existe
        fun copyDatabaseFromAssets(context: Context, dbName: String): Boolean {
            val dbFile = File(context.getDatabasePath(dbName).path)
            if (dbFile.exists()) {
                return true // La base de datos ya existe
            }

            try {
                val inputStream: InputStream = context.assets.open(dbName)
                val outputStream: OutputStream = FileOutputStream(dbFile)

                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

                return true
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }
        }

        fun exportarBaseDeDatosParaAssets(context: Context) {
            try {
                val dbFile = context.getDatabasePath("TPV.db")
                val destino = File(context.getExternalFilesDir(null), "TPV_para_assets.db")

                dbFile.copyTo(destino, overwrite = true)

                Log.d("TPVDebug", "Base exportada para assets en: ${destino.absolutePath}")
                // Luego puedes copiar ese archivo a tu proyecto en la carpeta assets manualmente
            } catch (e: Exception) {
                Log.e("TPVDebug", "Error al exportar DB: ${e.message}")
            }
        }

    }
}
