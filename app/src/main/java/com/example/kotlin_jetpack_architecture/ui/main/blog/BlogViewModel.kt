package com.example.kotlin_jetpack_architecture.ui.main.blog

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.example.kotlin_jetpack_architecture.models.BlogPost
import com.example.kotlin_jetpack_architecture.repository.main.BlogRepository
import com.example.kotlin_jetpack_architecture.session.SessionManager
import com.example.kotlin_jetpack_architecture.ui.BaseViewModel
import com.example.kotlin_jetpack_architecture.ui.DataState
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogStateEvent
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogStateEvent.*
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogViewState
import com.example.kotlin_jetpack_architecture.util.AbsentLiveData
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val blogRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val requestManager: RequestManager
): BaseViewModel<BlogStateEvent, BlogViewState>(){

    @InternalCoroutinesApi
    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {
        return when(stateEvent){

            is BlogSearchEvent ->{
                sessionManager.cachedToken.value?.let { authToken ->
                    blogRepository.searchBlogPost(
                        authToken,
                        viewState.value!!.blogFields.searchQuery
                    )
                }?: AbsentLiveData.create()

            }
            is CheckAuthorOfBlogPost ->{
                AbsentLiveData.create()
            }
            is None ->{
                AbsentLiveData.create()
            }
        }
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    fun setQuery(query: String){
        val update = getCurrentViewStateOrNew()
//        if(query == update.blogFields.searchQuery){
//            return
//        }
        update.blogFields.searchQuery = query
        _viewState.value = update
    }

    fun setBlogListData(blogList: List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogFields.blogList = blogList
        _viewState.value = update
    }

    fun setBlogPost(blogPost: BlogPost){
        val update = getCurrentViewStateOrNew()
        update.viewBlogFields.blogPost = blogPost
        _viewState.value = update
    }

    fun setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean){
        val update = getCurrentViewStateOrNew()
        update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
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
