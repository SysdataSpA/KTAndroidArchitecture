package it.sysdata.ktandroidarchitecturecore

import androidx.paging.*


open class BaseItemKeyedDatasource<Key:Any, Data : Any>: ItemKeyedDataSource<Key, Data>(){

    private var defaultLoadSize: Int? = null
    protected lateinit var datas: List<Data>

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Data>) {

        val requestedLoadSize = params.requestedLoadSize
        val key = params.key

        val item = getItemForKey(key)
        var startPosition = datas.indexOf(item)
        startPosition = if(startPosition < datas.size) startPosition else 0

        val subList = getSubList(startPosition, requestedLoadSize)

        callback.onResult(subList)
/*      TODO: check this code
        owner.loadData(range, {
            callback.onResult(it)
        })*/
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Data>) {
        val requestedLoadSize = params.requestedLoadSize
        val key = params.key

        val item = getItemForKey(key)
        var startPosition = datas.indexOf(item) - requestedLoadSize
        startPosition = if(startPosition > 0) startPosition else 0

        val subList = getSubList(startPosition, requestedLoadSize)

        callback.onResult(subList)
    }

    override fun getKey(item: Data): Key {
        return item.hashCode() as Key
    }

    override fun loadInitial(params: LoadInitialParams<Key>, callback: LoadInitialCallback<Data>) {
        val placeholdersEnabled = params.placeholdersEnabled
        val requestedInitialKey = params.requestedInitialKey
        var requestedLoadSize = params.requestedLoadSize

        val item = requestedInitialKey?.let { getItemForKey(it) }
        var startPosition = item?.let {datas.indexOf(item)} ?: 0
        startPosition = if(startPosition < datas.size) startPosition else 0

        defaultLoadSize?.let {
            if(requestedLoadSize > it){
                requestedLoadSize = it
            }
        }
        val subList = getSubList(startPosition, requestedLoadSize)

        if (placeholdersEnabled) {
            callback.onResult(subList, startPosition, datas.size)
        } else {
            callback.onResult(subList)
        }
    }

    // Override this method to use another key
    protected open fun getItemForKey(key: Key):Data{
        return datas.first { it.hashCode() == key.hashCode() }
    }

    private fun getSubList(startPosition: Int, defaultLoadSize: Int): List<Data> {
        if(startPosition >= datas.size){
            return datas.subList(datas.size - 1, datas.size - 1)
        }
        val endIndex = if (startPosition + defaultLoadSize <= datas.size) startPosition + defaultLoadSize else datas.size - 1
        return datas.subList(startPosition, endIndex)
    }

    fun init(datas: List<Data>, pageSize: Int) {
        this.datas = datas
        this.defaultLoadSize = pageSize
    }
}