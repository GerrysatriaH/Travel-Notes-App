package pnj.ti4a.uas_gerrysatriahalim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import pnj.ti4a.uas_gerrysatriahalim.fragment.AddNoteFragment
import pnj.ti4a.uas_gerrysatriahalim.fragment.ListNoteFragment
import pnj.ti4a.uas_gerrysatriahalim.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fm = supportFragmentManager.beginTransaction()
        fm.add(R.id.container, ListNoteFragment())
        fm.commit()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)

        val addNoteFragment = AddNoteFragment()
        val listNoteFragment = ListNoteFragment()
        val profileFragment = ProfileFragment()

        currentFragment(addNoteFragment)

        bottomNavigation.setOnItemSelectedListener() {
            when (it.itemId) {
                R.id.menu_createNote -> currentFragment(addNoteFragment)
                R.id.menu_listNote -> currentFragment(listNoteFragment)
                R.id.menu_profile -> currentFragment(profileFragment)
            }
            true
        }
    }

    private fun currentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
}