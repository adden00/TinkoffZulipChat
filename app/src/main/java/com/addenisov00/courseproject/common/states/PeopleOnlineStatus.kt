package com.addenisov00.courseproject.common.states

import com.addenisov00.courseproject.R

enum class PeopleOnlineStatus(val value: Int) {
    ONLINE(R.color.profile_online_status),
    OFFLINE(R.color.profile_offline_status),
    IDLE(R.color.profile_idle_status)
}