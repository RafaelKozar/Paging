package bakeapp.rako.paginglibrary_example

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import java.util.concurrent.Executor

/**
 * Created by rako on 13/07/2018.
 */
class InMemory(val api : MoviesApi, val executor: Executor) : MovieRepository {

    @MainThread
    override fun movies(type: String, pageSize: Int): Listing<MyMovie> {
        val dataSourceFactory = DataSourceFactory(api, type, executor)

        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPageSize(pageSize)
                .build()

        val pagedList = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
                // provide custom executor for network requests, otherwise it will default to
                // Arch Components' IO pool which is also used for disk access
                .setFetchExecutor(executor)
                .build()

        val refreshState = Transformations.switchMap(dataSourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return Listing(
                pagedList = pagedList,
                networkState = Transformations.switchMap(dataSourceFactory.sourceLiveData, {
                    it.networkState
                }),
                retry = {
                    dataSourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    dataSourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState
        )
    }
}