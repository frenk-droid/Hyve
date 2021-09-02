package com.example.login_example

import HomepageFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_homepage.*



class MainActivity2 : AppCompatActivity() {
   var User:user?= null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        User = intent.getSerializableExtra("USER_DATA") as user
        Log.d("User", User!!.image_profile)
        val firstFragment= homepage2()
        val secondFragment=HomepageFragment()
        val thirdFragment=homepage3()





        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(secondFragment)
                R.id.topic->setCurrentFragment(firstFragment)
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


    fun get():user{
        return User!!
    }

}
