package com.example.kotlin_jetpack_architecture.ui.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.example.kotlin_jetpack_architecture.models.BlogPost
import com.example.kotlin_jetpack_architecture.persistence.BlogQueryUtils
import com.example.kotlin_jetpack_architecture.repository.main.BlogRepository
import com.example.kotlin_jetpack_architecture.session.SessionManager
import com.example.kotlin_jetpack_architecture.ui.BaseViewModel
import com.example.kotlin_jetpack_architecture.ui.DataState
import com.example.kotlin_jetpack_architecture.ui.Loading
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogStateEvent
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogStateEvent.*
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogViewState
import com.example.kotlin_jetpack_architecture.util.AbsentLiveData
import com.example.kotlin_jetpack_architecture.util.PreferenceKeys.Companion.BLOG_FILTER
import com.example.kotlin_jetpack_architecture.util.PreferenceKeys.Companion.BLOG_ORDER
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val blogRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<BlogStateEvent, BlogViewState>(){


    init {
        setBlogFilter(
            sharedPreferences.getString(
                BLOG_FILTER,
                BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            )
        )
        sharedPreferences.getString(
            BLOG_ORDER,
            BlogQueryUtils.BLOG_ORDER_ASC
        )
    }


    @InternalCoroutinesApi
    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {
        return when (stateEvent) {

            is BlogSearchEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    blogRepository.searchBlogPost(
                        authToken,
                        getSearchQuery(),
                        getOrder() + getFilter(),
                        getPage()
                    )
                } ?: AbsentLiveData.create()

            }
            is CheckAuthorOfBlogPost -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    blogRepository.isAuthorOfBlogPost(
                        authToken,
                        slug = getSlug()
                    )
                } ?: AbsentLiveData.create()
            }
            is None -> {
                return object : LiveData<DataState<BlogViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value = DataState(null, Loading(false), null)
                    }
                }
            }
        }
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    fun saveFilterOptions(filter: String, order: String){
        editor.putString(BLOG_FILTER, filter)
        editor.apply()

        editor.putString(BLOG_ORDER, order)
        editor.apply()
    }


    fun setQuery(query: String){
        val update = getCurrentViewStateOrNew()
//        if(query == update.blogFields.searchQuery){
//            return
//        }
        update.blogFields.searchQuery = query
        _viewState.value = update
    }

    fun cancelActiveJobs(){
        blogRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}
