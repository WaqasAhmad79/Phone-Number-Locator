package com.example.phonenumberlocator.pnlHiltFolder

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    // We need this client to access user location
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context) = FusedLocationProviderClient(context)

   /* @ServiceScoped
    @Provides
    // This is the activity where our service belongs to
    fun providePendingIntent(@ApplicationContext context: Context): PendingIntent = PendingIntent.getActivity(
        context, 0,
        Intent(context, LTGpsTrackerActivity::class.java).also {
            it.action = TLGPsTrackerConstants.ACTION_SHOW_TRACKING_ACTIVITY
        }, PendingIntent.FLAG_IMMUTABLE
    )
*/
//    @ServiceScoped
//    @Provides
//    // Building the actual notification that will be displayed
//    fun provideBaseNotificationBuilder(@ApplicationContext context: Context, pendingIntent: PendingIntent) = NotificationCompat.Builder(context, TLGPsTrackerConstants.NOTIFICATION_CHANNEL_ID)
//        .setAutoCancel(false)
//        .setOngoing(true)
//        .setSmallIcon(R.drawable.ic_pin_trace_black_24dp)
//        .setContentTitle("Phone Tracker")
//        .setContentText("00:00:00")
//        .setContentIntent(pendingIntent)
//
//
}