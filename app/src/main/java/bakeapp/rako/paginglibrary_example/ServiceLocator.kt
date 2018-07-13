package bakeapp.rako.paginglibrary_example

import android.app.Application
import android.content.Context
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by rako on 13/07/2018.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator()
                }
                return instance!!
            }
        }
    }

//        fun getRepository(type: RedditPostRepository.Type): RedditPostRepository

        fun getRepository() : MovieRepository

        fun getNetworkExecutor(): Executor

        fun getApi(): MoviesApi
}

open class DefaultServiceLocator() : ServiceLocator {

    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    override fun getRepository(): MovieRepository {
        return  InMemory(getApi(), getNetworkExecutor())
    }

    override fun getNetworkExecutor(): Executor {
        return NETWORK_IO
    }

    override fun getApi(): MoviesApi {
        return  MoviesApi.create()
    }

}
