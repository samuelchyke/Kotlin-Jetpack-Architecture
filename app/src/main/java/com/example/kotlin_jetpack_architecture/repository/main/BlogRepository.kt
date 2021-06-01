package com.example.kotlin_jetpack_architecture.repository.main

import com.example.kotlin_jetpack_architecture.api.main.OpenApiMainService
import com.example.kotlin_jetpack_architecture.persistence.BlogPostDao
import com.example.kotlin_jetpack_architecture.repository.JobManager
import com.example.kotlin_jetpack_architecture.session.SessionManager
import javax.inject.Inject

private val TAG: String = "AppDebug"

class BlogRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("BlogRepository")
{




}