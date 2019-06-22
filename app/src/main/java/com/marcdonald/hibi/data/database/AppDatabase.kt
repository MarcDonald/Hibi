package com.marcdonald.hibi.data.database

interface AppDatabase {
  fun dao(): DAO
  fun closeDB()
}