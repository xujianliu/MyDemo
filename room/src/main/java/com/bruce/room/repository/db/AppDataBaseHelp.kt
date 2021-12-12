package com.bruce.room.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bruce.room.BruceApplication

/**
 *
 *@author xujian
 *@date 2021/11/30
 */
object AppDataBaseHelp {
    private lateinit var db: AppDatabase
    fun getDatabase(): AppDatabase {
        if (!this::db.isInitialized) {
            db = create()
        }
        return db
    }

    private fun create(): AppDatabase {
        //根据用户ID创建数据库
        val dbName = "userId_Database.db"
        return Room.databaseBuilder(BruceApplication.instance, AppDatabase::class.java, dbName)
            .build()
    }

//    @Database(entities = { YourEntity.class }, version = 1, exportSchema = false)

    @Database(entities = [User::class], version = 1,exportSchema = false) //映射数据库
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
    }
}