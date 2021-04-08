package com.erikaosgue.mychatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.erikaosgue.mychatapp.fragments.ChatsFragment
import com.erikaosgue.mychatapp.fragments.UsersFragment

class SectionPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return UsersFragment()
            1 -> return ChatsFragment()
        }
        return null!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> return "USERS"
            1 -> return "CHATS"
        }

        return null!!
    }


}