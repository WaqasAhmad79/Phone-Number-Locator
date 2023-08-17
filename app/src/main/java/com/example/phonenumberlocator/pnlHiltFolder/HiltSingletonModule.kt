package com.example.phonenumberlocator.pnlHiltFolder

import android.content.Context
import com.example.phonenumberlocator.pnlAdapter.PNLIsdStdAdapter
import com.example.phonenumberlocator.pnlAdapter.PNLPhoneContactsAdapter
import com.example.tracklocation.tlHelper.PNLMyContactsHelper
import com.example.tracklocation.tlRepo.PNLContactRepo
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.example.phonenumberlocator.pnlUtil.PNLDataStoreDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HiltSingletonModule {
    @Provides
    @Singleton
    fun providesContactsHelper(@ApplicationContext context: Context): PNLMyContactsHelper {
        return PNLMyContactsHelper(context)
    }


    @Provides
    @Singleton
    fun providesContactRepo(
        contactHelper: PNLMyContactsHelper,
    ): PNLContactRepo {
        return PNLContactRepo(contactHelper)
    }


    @Provides
    @Singleton
    fun providesCheckInternetConnection(@ApplicationContext context: Context): PNLCheckInternetConnection {
        return PNLCheckInternetConnection(context)
    }

    @Provides
    fun providesPNLIsdStdAdapter(@ApplicationContext context: Context): PNLIsdStdAdapter {
        return PNLIsdStdAdapter(context)
    }


    @Provides
    fun providesPNLPhoneContactsAdapter(@ApplicationContext context: Context): PNLPhoneContactsAdapter {
        return PNLPhoneContactsAdapter(context)
    }
//
//    @Provides
//    fun providesDialogContactsAdapter(@ApplicationContext context: Context): DialogContactsAdapter {
//        return DialogContactsAdapter(context)
//    }
//
//    @Provides
//    fun providesDialogClockAdapter(@ApplicationContext context: Context): DialogAddClockAdapter {
//        return DialogAddClockAdapter(context)
//    }
//
//    @Provides
//    fun providesUserAddClocksAdapter(@ApplicationContext context: Context): UserAddClocksAdapter {
//        return UserAddClocksAdapter(context)
//    }

    /*   @Provides
       fun providesFavoriteContactsAdapter(@ApplicationContext context: Context): FavoriteContactsAdapter {
           return FavoriteContactsAdapter(context)
       }*/
    /*
      @Provides
      fun providesGpsHistoryAdapter(@ApplicationContext context: Context): GpsRecordAdapter {
          return GpsRecordAdapter(context)
      }

      @Provides
      fun providesNearbyPlacesAdapter(@ApplicationContext context: Context): NearbyPlacesAdapter {
          return NearbyPlacesAdapter(context)
      }*/

    @Provides
    @Singleton
    fun providesDataStoreDb(@ApplicationContext context: Context): PNLDataStoreDb {
        return PNLDataStoreDb(context)
    }


}