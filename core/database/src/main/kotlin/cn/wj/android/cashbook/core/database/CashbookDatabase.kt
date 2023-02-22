package cn.wj.android.cashbook.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.wj.android.cashbook.core.database.dao.AssetDao
import cn.wj.android.cashbook.core.database.dao.TypeDao
import cn.wj.android.cashbook.core.database.table.AssetTable
import cn.wj.android.cashbook.core.database.table.BooksTable
import cn.wj.android.cashbook.core.database.table.RecordTable
import cn.wj.android.cashbook.core.database.table.TagTable
import cn.wj.android.cashbook.core.database.table.TypeTable

/**
 * 记账本数据库
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2021/5/15
 */
@Database(
    entities = [BooksTable::class, AssetTable::class, TypeTable::class, RecordTable::class, TagTable::class],
    version = 7
)
abstract class CashbookDatabase : RoomDatabase() {

    /** 获取类型数据库操作接口 */
    abstract fun typeDao(): TypeDao

    /** 获取资产数据库操作接口 */
    abstract fun assetDao(): AssetDao

}