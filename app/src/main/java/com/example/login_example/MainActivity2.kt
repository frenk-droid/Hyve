package com.example.login_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_homepage.*

class MainActivity2 : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val firstFragment=HomepageFragment()
        val secondFragment=homepage2()
        val thirdFragment=homepage3()
        val settingFragment=settings()

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

    }
