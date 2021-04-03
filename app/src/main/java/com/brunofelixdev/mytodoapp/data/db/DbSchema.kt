package com.brunofelixdev.mytodoapp.data.db

object DbSchema {

    //  Database parameters
    const val DB_NAME: String = "app_database.db"
    const val DB_VERSION: Int = 1

    //  Table names
    const val TB_ITEMS: String = "items"

    //  Queries
    const val QUERY_ORDER_BY_NAME = "SELECT * FROM items WHERE isDone == 0 ORDER BY name ASC"
    const val QUERY_ORDER_BY_DATETIME = "SELECT * FROM items WHERE isDone == 0 ORDER BY date(dueDateTime) ASC"

}