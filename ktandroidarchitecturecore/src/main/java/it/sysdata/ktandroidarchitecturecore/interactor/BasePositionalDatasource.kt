package it.sysdata.ktandroidarchitecturecore.interactor

import androidx.paging.PositionalDataSource


open class BasePositionalDatasource<Data : Any>: PositionalDataSource<Data>(){

    private var defaultLoadSize: Int? = null
    private lateinit var datas: List<Data>
    private var currentPosition: Int = 0

    fun initDataSource(datas: List<Data>, defaultLoadSize: Int? = null){
        this.datas = datas
        this.defaultLoadSize = defaultLoadSize
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Data>) {
        val loadSize = params.loadSize
        val startPosition = params.startPosition

        val subList = if(currentPosition > 0 && defaultLoadSize != null){
            getSubList(currentPosition, defaultLoadSize as Int)
        } else{
            getSubList(startPosition, loadSize)
        }

        callback.onResult(subList)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Data>) {
        var startPosition = params.requestedStartPosition
        var pageSize = params.pageSize
        val placeholdersEnabled = params.placeholdersEnabled
        val requestedLoadSize = params.requestedLoadSize

        startPosition = if(startPosition < datas.size) startPosition else 0

        defaultLoadSize?.let {
            if(pageSize > it){
                pageSize = it
            }
        }
        val subList = getSubList(startPosition, requestedLoadSize)
        if(placeholdersEnabled){
            callback.onResult(subList, startPosition, requestedLoadSize)
        } else {
            callback.onResult(subList, startPosition)
        }
    }

    private fun getSubList(startPosition: Int, defaultLoadSize: Int): List<Data> {
        if(startPosition >= datas.size){
            return datas.subList(datas.size - 1, datas.size - 1)
        }
        val endIndex = if (startPosition + defaultLoadSize <= datas.size) startPosition + defaultLoadSize else datas.size - 1
        currentPosition = endIndex
        return datas.subList(startPosition, endIndex)
    }
}