package com.example.phonenumberlocator.pnlUtil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

abstract class PNLGenericAdapter <T>
    :RecyclerView.Adapter<RecyclerView.ViewHolder>, Filterable{
    var listItems: MutableList<T>
    // Single not-to-be-modified copy of original data in the list.
    var originalList : MutableList<T> = mutableListOf()
    // a method-body to invoke when search returns nothing. It can be null.
    private var onNothingFound: ((isFound : Boolean) -> Unit)?= null
    constructor(items: MutableList<T>){
        this.listItems= items
        this.originalList = ArrayList(listItems)
    }
    constructor(){
        listItems = mutableListOf()
        this.originalList = ArrayList(listItems)
    }

    fun setItems(items : MutableList<T>){
        this.listItems= items
        this.originalList = ArrayList(listItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(LayoutInflater.from(parent.context)
            .inflate(viewType, parent,false), viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(listItems[position])
    }

    override fun getItemCount() = listItems.size

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }

    fun search(s : String? ,onNothingFound : ((isFound : Boolean)-> Unit)?){
        this.onNothingFound = onNothingFound
        filter.filter(s)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults { listItems.clear()
                if(constraint.isNullOrBlank()){
                    listItems.addAll(originalList)
                }else{
                    val searchResult = originalList.filter {
                        (it as Searchable).getSearchCriteria().contains(constraint , ignoreCase = true)
                    }

                    listItems.addAll(searchResult)
                }
                return filterResults.also { it.values = listItems }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                if(listItems.isNullOrEmpty()){
                    onNothingFound?.invoke(false)
                }else{
                    onNothingFound?.invoke(true)
                }
                notifyDataSetChanged()
                //listItems= results?.values as MutableList<T>

            }

        }
    }

    protected abstract fun getLayoutId(position : Int , obj : T) : Int
    abstract fun getViewHolder(view : View , viewType : Int) : RecyclerView.ViewHolder
    internal interface Binder<T>{
        fun bind(data : T)
    }

    internal interface Searchable{
        fun getSearchCriteria() : String
    }
}