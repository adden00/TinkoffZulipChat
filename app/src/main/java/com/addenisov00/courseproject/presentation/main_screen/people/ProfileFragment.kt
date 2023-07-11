package com.addenisov00.courseproject.presentation.main_screen.people

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.common.Constants.PROFILE_ITEM_KEY
import com.addenisov00.courseproject.common.di.ui.DaggerPeopleComponent
import com.addenisov00.courseproject.common.states.PeopleOnlineStatus
import com.addenisov00.courseproject.databinding.FragmentProfileBinding
import com.addenisov00.courseproject.getAppComponent
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleEffect
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleEvent
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleState
import com.bumptech.glide.Glide
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ProfileFragment : ElmFragment<PeopleEvent, PeopleEffect, PeopleState>() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userId: Int by lazy {
        arguments?.getInt(PROFILE_ITEM_KEY) ?: Constants.MY_USER_ID
    }

    override val initEvent: PeopleEvent
        get() = PeopleEvent.Ui.Init

    @Inject
    lateinit var viewModelFactory: PeopleStoreViewModelFactory
    private val viewModel: PeopleStoreViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerPeopleComponent.factory().create(requireContext().getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUi() {
        binding.swipeToRefresh.setOnRefreshListener {
            store.accept(PeopleEvent.Ui.LoadPerson(userId))
        }
    }

    override fun createStore(): Store<PeopleEvent, PeopleEffect, PeopleState> {
        return viewModel.peopleStore()
    }

    override fun render(state: PeopleState) {
        if (state.currentPerson == null)
            store.accept(PeopleEvent.Ui.LoadPerson(userId))
        else {
            setData(state.currentPerson)
        }
    }

    override fun handleEffect(effect: PeopleEffect) {
        when (effect) {
            is PeopleEffect.Error -> showErrorToast(effect.e)
            is PeopleEffect.ShowLoader -> showLoader()
            is PeopleEffect.HideLoader -> hideLoader()
        }
    }

    private fun showErrorToast(e: Throwable) {
        Toast.makeText(requireContext(), "error: $e", Toast.LENGTH_SHORT).show()
    }

    private fun showLoader() {
        binding.profileInfo.visibility = View.GONE
        binding.pbarProfileLoading.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.profileInfo.visibility = View.VISIBLE
        binding.pbarProfileLoading.visibility = View.GONE
        binding.swipeToRefresh.isRefreshing = false
    }

    private fun setData(profileItem: PersonItem) {
        binding.tvName.text = profileItem.name
        binding.tvStatus.text = profileItem.email
        Glide.with(requireContext()).load(profileItem.photo).centerCrop()
            .into(binding.imProfilePhoto)
        binding.tvOnlineStatus.text = when (profileItem.status) {
            PeopleOnlineStatus.ONLINE.value -> "online"
            PeopleOnlineStatus.OFFLINE.value -> "offline"
            PeopleOnlineStatus.IDLE.value -> "idle"
            else -> throw Exception("wrong status!")
        }
        binding.tvOnlineStatus.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                profileItem.status
            )
        )
    }
}