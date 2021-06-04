package com.example.kotlin_jetpack_architecture.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.models.BlogPost
import com.example.kotlin_jetpack_architecture.ui.AreYouSureCallback
import com.example.kotlin_jetpack_architecture.ui.UIMessage
import com.example.kotlin_jetpack_architecture.ui.UIMessageType
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogStateEvent
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogStateEvent.*
import com.example.kotlin_jetpack_architecture.ui.main.blog.viewmodel.*
import com.example.kotlin_jetpack_architecture.util.DateUtils
import com.example.kotlin_jetpack_architecture.util.SuccessHandling.Companion.SUCCESS_BLOG_DELETED
import kotlinx.android.synthetic.main.fragment_view_blog.*
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*

class ViewBlogFragment : BaseBlogFragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        CheckAuthorOfBlogPost()
        subscribeObservers()
        stateChangeListener.expandAppBar()

        delete_button.setOnClickListener {
            deleteBlogPost()
        }

    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let{data->
                data.data?.getContentIfNotHandled()?.let{ viewState ->
                    viewState.viewBlogFields.isAuthorOfBlogPost
                }
                data.response?.peekContent()?.let{response ->
                    if(response.message == SUCCESS_BLOG_DELETED){
                        viewModel.removeDeletedBlogPost()
                        findNavController().popBackStack()
                    }
                }
            }


        })

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            viewState.viewBlogFields.blogPost?.let{ blogPost ->
                setBlogProperties(blogPost)
                if(viewState.viewBlogFields.isAuthorOfBlogPost){
                    adaptViewToAuthorMode()
                }
            }
        })
    }


    fun deleteBlogPost(){
        viewModel.setStateEvent(
            DeleteBlogPostEvent()
        )
    }

    fun confirmDeleteRequest(){
        val callback: AreYouSureCallback = object: AreYouSureCallback {

            override fun proceed() {
                deleteBlogPost()
            }

            override fun cancel() {
                // ignore
            }

        }
        uiCommunicationListener.onUIMessageReceived(
            UIMessage(
                getString(R.string.are_you_sure_delete),
                UIMessageType.AreYouSureDialog(callback)
            )
        )
    }

    private fun adaptViewToAuthorMode() {
        activity?.invalidateOptionsMenu()
        delete_button.visibility = View.VISIBLE
    }

    private fun setBlogProperties(blogPost: BlogPost){
        requestManager
            .load(blogPost.image)
            .into(blog_image)

        blog_title.text = blogPost.title
        blog_author.text = blogPost.username
        blog_update_date.text = DateUtils.convertLongToStringDate(blogPost.date_updated)
        blog_body.text = blogPost.body
    }

    fun checkIsAuthorOfBlogPost(){
        viewModel.setIsAuthorOfBlogPost(false)
        viewModel.setStateEvent(CheckAuthorOfBlogPost())
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        if(viewModel.isAuthorOfBlogPost()){
              inflater.inflate(R.menu.edit_view_menu, menu)
        }
        
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(viewModel.isAuthorOfBlogPost()){
            when(item.itemId){
                R.id.edit -> {
                    navUpdateBlogFragment()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navUpdateBlogFragment(){
        try{
            viewModel.setUpdatedBlogFields(
                    viewModel.getBlogPost().title,
                    viewModel.getBlogPost().body,
                    viewModel.getBlogPost().image.toUri()
                    )
        }catch (e: Exception){
            Log.e(TAG, "Exception:${e.message} ")
        }
        findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
    }
}