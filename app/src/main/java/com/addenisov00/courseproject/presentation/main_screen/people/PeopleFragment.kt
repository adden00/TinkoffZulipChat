package com.addenisov00.courseproject.presentation.main_screen.people

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.Constants.PROFILE_ITEM_KEY
import com.addenisov00.courseproject.common.OnClickListener
import com.addenisov00.courseproject.common.delegate_utills.BaseAdapter
import com.addenisov00.courseproject.common.di.ui.DaggerPeopleComponent
import com.addenisov00.courseproject.databinding.FragmentPeopleBinding
import com.addenisov00.courseproject.getAppComponent
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleEffect
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleEvent
import com.addenisov00.courseproject.presentation.main_screen.people.elm.PeopleState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class PeopleFragment : ElmFragment<PeopleEvent, PeopleEffect, PeopleState>() {


    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val peopleAdapter: BaseAdapter by lazy { BaseAdapter() }

    @Inject
    lateinit var viewModelFactory: PeopleStoreViewModelFactory
    private val viewModel: PeopleStoreViewModel by viewModels { viewModelFactory }

    override val initEvent: PeopleEvent
        get() = PeopleEvent.Ui.Init


    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerPeopleComponent.factory().create(requireContext().getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUI() {
        binding.rcPeople.adapter = peopleAdapter.apply {
            addDelegate(PeopleAdapter(PersonClickListener()))
        }
        binding.swipeToRefresh.setOnRefreshListener {
            store.accept(PeopleEvent.Ui.LoadPeople)
        }
        binding.editTextTextPersonName.addTextChangedListener {
            store.accept(PeopleEvent.Ui.SearchPeople(it?.toString() ?: ""))
        }
    }

    override fun createStore(): Store<PeopleEvent, PeopleEffect, PeopleState> {
        return viewModel.peopleStore()
    }

    override fun render(state: PeopleState) {
        if (state.peopleList == null)
            store.accept(PeopleEvent.Ui.LoadPeople)
        else {
            if (state.isSearching)
                peopleAdapter.submitList(state.peopleSearchedList)
            else
                peopleAdapter.submitList(state.peopleList)
        }
    }

    override fun handleEffect(effect: PeopleEffect) {
        when (effect) {
            is PeopleEffect.Error -> showErrorToast(effect.e)
            is PeopleEffect.ShowLoader -> showLoader()
            is PeopleEffect.HideLoader -> hideLoader()
        }
    }

    private fun showLoader() {
        if (store.currentState.peopleList.isNullOrEmpty()) {
            binding.rcPeople.visibility = View.GONE
            binding.peopleShimmerList.shimmer.visibility = View.VISIBLE
        }
    }

    private fun hideLoader() {
        binding.rcPeople.visibility = View.VISIBLE
        binding.peopleShimmerList.shimmer.visibility = View.GONE
        binding.swipeToRefresh.isRefreshing = false
    }

    private fun showErrorToast(e: Throwable) {
        Toast.makeText(requireContext(), "error: $e", Toast.LENGTH_SHORT).show()
    }


    inner class PersonClickListener : OnClickListener<PersonItem> {
        override fun onClick(item: PersonItem) {
            val bundle = bundleOf(PROFILE_ITEM_KEY to item.id)
            (requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController.navigate(
                R.id.action_mainFragment_to_profileFragment2,
                bundle
            )
        }
    }
}