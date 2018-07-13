package bakeapp.rako.paginglibrary_example

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * Created by rako on 13/07/2018.
 */
class DataSourceItemMovie(private val type : String,
                          private val api : MoviesApi,
                          private val executor : Executor) : ItemKeyedDataSource<String, MyMovie>() {

    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    var page = 0

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            executor.execute {
                it.invoke()
            }
        }
    }


    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<MyMovie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<MyMovie>) {

        val request = api.getMovies(
                type, mdbKey, language, page.toString()
        )

        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)


        try {
            val response = request.execute()
            val items = response.body()?.results ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<MyMovie>) {
        // set network value to loading.
        networkState.postValue(NetworkState.LOADING)

        // even though we are using async retrofit API here, we could also use sync
        // it is just different to show that the callback can be called async.
        api.getMovies(type, mdbKey, language, (page).toString()) //implement add ++ in page
                .enqueue(object : retrofit2.Callback<Movies> {

                    override fun onFailure(call: Call<Movies>?, t: Throwable?) {
                        retry = {loadAfter(params, callback)}
                        networkState.postValue(NetworkState.error(t?.message ?: "unknown err"))
                    }

                    override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                        if (response.isSuccessful) {

                            val items = response.body()?.results ?: emptyList()
                            // clear retry since last request succeeded
                            retry = null
                            callback.onResult(items)
                            networkState.postValue(NetworkState.LOADED)
                        } else {

                            retry = {loadAfter(params, callback)}
                            networkState.postValue(
                                    NetworkState.error("error code: ${response.code()}"))
                        }
                    }

                })
    }

    override fun getKey(item: MyMovie): String {
        return item.id!!
    }
}