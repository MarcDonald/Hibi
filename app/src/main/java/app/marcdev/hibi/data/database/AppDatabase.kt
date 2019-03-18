package app.marcdev.hibi.data.database

interface AppDatabase {
  fun dao(): DAO
  fun checkpoint()
  fun closeDB()
}