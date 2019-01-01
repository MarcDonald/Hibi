package app.marcdev.nikki.data.database

interface AppDatabase {
  fun dao(): DAO
}