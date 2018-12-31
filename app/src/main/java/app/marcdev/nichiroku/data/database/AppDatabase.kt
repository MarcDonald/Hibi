package app.marcdev.nichiroku.data.database

interface AppDatabase {
  fun dao(): DAO
}