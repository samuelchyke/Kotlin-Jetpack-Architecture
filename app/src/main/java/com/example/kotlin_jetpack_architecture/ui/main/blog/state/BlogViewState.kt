package com.example.kotlin_jetpack_architecture.ui.main.blog.state

import com.example.kotlin_jetpack_architecture.models.BlogPost

import com.example.kotlin_jetpack_architecture.persistence.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.example.kotlin_jetpack_architecture.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED

data class BlogViewState (

    // BlogFragment vars
    var blogFields: BlogFields = BlogFields(),

    // ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields()

    // ViewBlogFragment vars

)
{
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList<BlogPost>(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var filter: String = ORDER_BY_ASC_DATE_UPDATED,
        var order: String = BLOG_ORDER_ASC
    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false
    )


}