package com.example.kotlin_jetpack_architecture.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.ui.BaseActivity
import com.example.kotlin_jetpack_architecture.ui.auth.AuthActivity
import com.example.kotlin_jetpack_architecture.ui.main.account.BaseAccountFragment
import com.example.kotlin_jetpack_architecture.ui.main.account.ChangePasswordFragment
import com.example.kotlin_jetpack_architecture.ui.main.account.UpdateAccountFragment
import com.example.kotlin_jetpack_architecture.ui.main.blog.BaseBlogFragment
import com.example.kotlin_jetpack_architecture.ui.main.blog.BaseCreateBlogFragment
import com.example.kotlin_jetpack_architecture.ui.main.blog.UpdateBlogFragment
import com.example.kotlin_jetpack_architecture.ui.main.blog.ViewBlogFragment
import com.example.kotlin_jetpack_architecture.util.BottomNavController
import com.example.kotlin_jetpack_architecture.util.BottomNavController.*
import com.example.kotlin_jetpack_architecture.util.setUpNavigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() ,
NavGraphProvider,
    OnNavigationGraphChanged,
        OnNavigationReselectedListener
{

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE){
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.nav_blog,
            this,
            this
        )
    }

    override fun displayProgressBar(bool: Boolean) {
        if (bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.INVISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if(savedInstanceState == null){
            bottomNavController.onNavigationItemSelected()
        }

        subscribeObservers()
        setupActionBar()
        Log.d(TAG, "Main: Token: ${sessionManager.cachedToken}: ")
    }

    private fun subscribeObservers(){
        sessionManager.cachedToken.observe(this, { authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: $authToken")
            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupActionBar(){
        setSupportActionBar(tool_bar)
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    override fun getNavGraphId(itemId: Int):Int = when(itemId){
        R.id.nav_blog -> {
            R.navigation.nav_blog
        }
        R.id.nav_create_blog -> {
            R.navigation.nav_create_blog
        }
        R.id.nav_account -> {
            R.navigation.nav_account
        }
        else -> {
            R.navigation.nav_blog
        }
    }

    override fun onGraphChange() {
        expandAppBar()
        cancelActiveJobs()
    }

    private fun cancelActiveJobs() {
        val fragments = bottomNavController.fragmentManager
            .findFragmentById(bottomNavController.containerId)
            ?.childFragmentManager
            ?.fragments
        if(fragments != null){
            for(fragment in fragments){
                when(fragment){
                    is BaseAccountFragment -> fragment.cancelActiveJobs()
                    is BaseBlogFragment-> fragment.cancelActiveJobs()
                    is BaseCreateBlogFragment-> fragment.cancelActiveJobs()
                    }
                }
            }
        displayProgressBar(false)
        }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment)
    = when(fragment){

        is ViewBlogFragment -> {
            navController.navigate(R.id.action_viewBlogFragment_to_home)
        }

        is UpdateBlogFragment -> {
            navController.navigate(R.id.action_updateBlogFragment_to_home)
        }

        is UpdateAccountFragment -> {
            navController.navigate(R.id.action_updateAccountFragment_to_home)
        }

        is ChangePasswordFragment -> {
            navController.navigate(R.id.action_changePasswordFragment_to_home)
        }

        else -> {
            // do nothing

    }
    }
}