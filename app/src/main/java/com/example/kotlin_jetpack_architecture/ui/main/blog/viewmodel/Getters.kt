package com.example.kotlin_jetpack_architecture.ui.main.blog.viewmodel

import android.net.Uri
import com.example.kotlin_jetpack_architecture.models.BlogPost

fun BlogViewModel.getSearchQuery(): String{
    getCurrentViewStateOrNew().let{
        return it.blogFields.searchQuery
    }
}

fun BlogViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let{
        return it.blogFields.page
    }
}

fun BlogViewModel.getIsQueryExhausted(): Boolean {
    getCurrentViewStateOrNew().let {
        return it.blogFields.isQueryExhausted
    }
}

fun BlogViewModel.getIsQueryInProgress(): Boolean {
    getCurrentViewStateOrNew().let {
        return it.blogFields.isQueryInProgress
    }
}

fun BlogViewModel.getFilter(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.filter
    }
}

fun BlogViewModel.getOrder(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.order
    }
}

fun BlogViewModel.getSlug(): String {
    getCurrentViewStateOrNew().let { blogViewState ->
        blogViewState.viewBlogFields.blogPost?.let{
            return it.slug
        }
    }
    return ""
}

fun BlogViewModel.isAuthorOfBlogPost(): Boolean{
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.isAuthorOfBlogPost
    }
}

fun BlogViewModel.getBlogPost(): BlogPost{
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.let{
            return it
        }?:getDummyBlogPost()
    }
}

fun BlogViewModel.getDummyBlogPost(): BlogPost {
    return BlogPost(-1,"","","","",-1, "")

}

fun BlogViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let { blogViewState ->
        blogViewState.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}
