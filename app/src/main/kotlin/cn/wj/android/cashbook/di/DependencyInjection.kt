package cn.wj.android.cashbook.di

import androidx.room.Room
import cn.wj.android.cashbook.data.constants.DB_FILE_NAME
import cn.wj.android.cashbook.data.database.CashbookDatabase
import cn.wj.android.cashbook.data.store.LocalDataStore
import cn.wj.android.cashbook.manager.AppManager
import cn.wj.android.cashbook.ui.viewmodel.ConsumptionTypeViewModel
import cn.wj.android.cashbook.ui.viewmodel.DateTimePickerViewModel
import cn.wj.android.cashbook.ui.viewmodel.EditBooksViewModel
import cn.wj.android.cashbook.ui.viewmodel.EditRecordViewModel
import cn.wj.android.cashbook.ui.viewmodel.GeneralViewModel
import cn.wj.android.cashbook.ui.viewmodel.MainViewModel
import cn.wj.android.cashbook.ui.viewmodel.MyBooksViewModel
import cn.wj.android.cashbook.ui.viewmodel.SelectAssetClassificationViewModel
import cn.wj.android.cashbook.ui.viewmodel.SelectAssetViewModel
import cn.wj.android.cashbook.ui.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * 依赖注入模块
 *
 * > [jiewang41](mailto:jiewang41@iflytek.com) 创建于 20201/5/11
 */

/** 数据库相关依赖注入 */
val dbModule = module {
    single {
        Room.databaseBuilder(
            AppManager.getContext().applicationContext,
            CashbookDatabase::class.java,
            DB_FILE_NAME
        ).build()
    }
}

/** 数据存储相关依赖注入 */
val dataStoreModule = module {
    factory {
        LocalDataStore(get())
    }
}

/** ViewModel 相关依赖注入 */
val viewModelModule = module {
    // Activity
    viewModel {
        SplashViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        MyBooksViewModel(get())
    }
    viewModel {
        EditBooksViewModel(get())
    }
    viewModel {
        EditRecordViewModel(get())
    }

    // Fragment
    viewModel {
        ConsumptionTypeViewModel(get())
    }

    // Dialog
    viewModel {
        GeneralViewModel()
    }
    viewModel {
        DateTimePickerViewModel()
    }
    viewModel {
        SelectAssetViewModel(get())
    }
    viewModel {
        SelectAssetClassificationViewModel(get())
    }
}