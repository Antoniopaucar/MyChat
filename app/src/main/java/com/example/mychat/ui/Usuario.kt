package com.example.mychat.ui

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Database
import androidx.room.RoomDatabase

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val usuario: String,
    val clave: String,
    val correo: String
)

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios")
    suspend fun getAll(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND clave = :clave LIMIT 1")
    suspend fun login(usuario: String, clave: String): Usuario?

    @Insert
    suspend fun insert(usuario: Usuario): Long

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)
}

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class MyChatDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
} 