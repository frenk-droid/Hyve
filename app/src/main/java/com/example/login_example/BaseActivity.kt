package com.example.login_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_homepage.*
import postPage


class BaseActivity : AppCompatActivity() {
   var user:User?= null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        val firstFragment=postPage()
        val secondFragment=topicPage()
        val thirdFragment=profilePage()
        user = intent.getSerializableExtra("USER_DATA") as User

        setCurrentFragment(firstFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(firstFragment)
                R.id.topic->setCurrentFragment(secondFragment)
                R.id.profile->setCurrentFragment(thirdFragment)

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.homeContainer,fragment)
            commit()
        }


    fun get():User{
        return user!!
    }

}
