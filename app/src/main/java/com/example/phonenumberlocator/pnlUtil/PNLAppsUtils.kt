package com.example.phonenumberlocator.pnlUtil

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.phonenumberlocator.R


object PNLAppsUtils {
    fun shareApp(context: Context) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey check out my app at: https://play.google.com/store/apps/details?id=" + context.packageName
        )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }
    fun rateUs(context: Context) {
        val uri = Uri.parse("market://details?id=" + context.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                )
            )
        }

    }

    fun updateApp(context: Context){
        rateUs(context)
    }

   /* fun privacyPolicy(context: Context) {
        val url = context.resources.getString(R.string.privacy_policy_link)
        var webpage = Uri.parse(url)

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val browserIntent = Intent(Intent.ACTION_VIEW,webpage )

        if (browserIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(browserIntent)
        }
    }

*/
    fun Activity.openLink(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        this.startActivity(browserIntent)
    }


    fun feedback(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        val recipients = arrayOf("ericberg.leeds@gmail.com")
        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, "" + context.resources.getString(R.string.app_name))
        intent.type = "text/plain"
        context.startActivity(Intent.createChooser(intent, "Send mail"))
    }



    fun openUrl(context: Context , url : String) {
        var webpage = Uri.parse(url)

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url) )

        if (browserIntent.resolveActivity(context.packageManager) != null) {
            browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(browserIntent)
        }
    }
    fun shareLocation(context: Context,shareLocUrl:String)
    {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT, shareLocUrl)
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }
    fun MoreApps(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "market://search?q=pub:" +
                                context.resources.getString(R.string.account_name)
                    )
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://play.google.com/store/apps/developer?id=" + context.resources.getString(
                            R.string.account_name
                        )
                    )
                )
            )
        }
    }


}
