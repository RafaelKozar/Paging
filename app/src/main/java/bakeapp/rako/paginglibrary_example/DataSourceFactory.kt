package bakeapp.rako.paginglibrary_example

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import java.util.concurrent.Executor

/**
 * Created by rako on 13/07/2018.
 */

class DataSourceFactory(val api: MoviesApi,
                        val type: String,
                        val executor: Executor) : DataSource.Factory<String, MyMovie> {

    val sourceLiveData = MutableLiveData<DataSourceItemMovie>()

    override fun create(): DataSource<String, MyMovie> {
        val source = DataSourceItemMovie(type, api, executor)
        sourceLiveData.postValue(source)
        return source

    }
}
