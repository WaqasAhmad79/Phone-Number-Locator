package com.example.phonenumberlocator.pnlAdapter

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ItemContactListBinding
import com.example.phonenumberlocator.pnlExtensionFun.highlightTextFromNumbers
import com.example.phonenumberlocator.pnlExtensionFun.highlightTextPart
import com.example.phonenumberlocator.pnlModel.PNLMyContact
import com.example.phonenumberlocator.ui.activities.callLocator.PNLPhoneContactsActivity
import com.example.tracklocation.tlHelper.PNLMyContactsHelper


class PNLPhoneContactsAdapter(val context: Context) :
    RecyclerView.Adapter<PNLPhoneContactsAdapter.ContactListViewHolder>() {
    lateinit var activity: Activity
    private var contacts: ArrayList<PNLMyContact> = arrayListOf()
    private var itemClick: ((Any, Int) -> Unit)? = null
    private var viewMoreClick: ((Any, Int) -> Unit)? = null
    var textToHighlight = ""

    fun textChanged(text: String) {
        val texts = text.removePrefix("+")
        textToHighlight = texts.removePrefix("*")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val view = ItemContactListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContactListViewHolder(view)
    }

    override fun getItemCount() = contacts.size

    fun setData(activity: PNLPhoneContactsActivity, itemList: ArrayList<PNLMyContact>) {
        Log.d("gssssss", "setData: ${itemList.size}")
        this.activity = activity
        contacts = itemList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(callBackNew: ((Any, Int) -> Unit)) {
        itemClick = callBackNew
    }

    fun setOnMoreClickListener(callBackNew: ((Any, Int) -> Unit)) {
        viewMoreClick = callBackNew
    }

    fun updateContacts(newContacts: ArrayList<PNLMyContact>) {
        val oldHashCode = contacts.hashCode()
        val newHashCode = newContacts.hashCode()
        if (newHashCode != oldHashCode) {
            contacts = newContacts
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bindItems(position, contact)
    }

    override fun onViewRecycled(holder: ContactListViewHolder) {
        super.onViewRecycled(holder)
        if (!activity.isDestroyed && !activity.isFinishing) {
            Glide.with(activity).clear(holder.itemView.findViewById<ImageView>(R.id.user_icon))
        }
    }

    inner class ContactListViewHolder(val binding: ItemContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(position: Int, contact: PNLMyContact) {
            binding.contactName.apply {
                text = if (contact.name.contains(textToHighlight, true)) {
                    contact.name.highlightTextPart(
                        textToHighlight, context.resources.getColor(R.color.selected_num)
                    )
                } else {
                    contact.name.highlightTextFromNumbers(
                        textToHighlight, context.resources.getColor(R.color.selected_num)
                    )
                }
            }
            binding.contactLocation.apply {
                val number = TextUtils.join(", ", contact.phoneNumbers.map { it.normalizedNumber })
                Log.d("TAGnum", "bindItems: $number")
                text = number.highlightTextPart(
                    textToHighlight,
                    context.resources.getColor(R.color.selected_num)
                )
                contact.name.highlightTextFromNumbers(
                    textToHighlight,
                    context.resources.getColor(R.color.selected_num)
                )
            }

//            binding.contactLocation.apply {
//                val phoneNumber=contact.phoneNumbers.first().normalizedNumber
//                text=phoneNumber
//            }
            //for contact location
/*            binding.contactLocation.apply {
                text = TextUtils.join(", ", contact.phoneNumbers.map { it.normalizedNumber })
            }*/


            //for user icon change
           PNLMyContactsHelper(activity).loadContactImageWithTextColor(binding.userIcon, contact.name)


            binding.ivInfo.setOnClickListener {
                viewMoreClick?.invoke(contact, position)

            }
            binding.root.setOnClickListener {
                viewMoreClick?.invoke(contact, position)

            }


//                if (checkIsValidNumber(contact.phoneNumbers.first().normalizedNumber)) {
//                    Log.d("testing", "setupView: ${contact.phoneNumbers.first().normalizedNumber}")
//                    val swissNumberProto: Phonenumber.PhoneNumber =
//                        PhoneNumberUtil.getInstance().parse(
//                            contact.phoneNumbers.first().normalizedNumber,
//                            TrackerAppClass.context.countryIso.uppercase()
//                        )
//                    Log.d("testing", "setupView: $swissNumberProto")
//                    TrackerAppClass.instance?.findCountryByDialCode("+${swissNumberProto.countryCode}") { countryModel ->
//                        binding.contactLocation.text = countryModel.name
//                    }
//                } else {
//                    binding.contactLocation.text = " "
//                }
        }

    }
}



