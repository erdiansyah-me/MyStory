package com.erdiansyah.mystory.presenter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erdiansyah.mystory.R
import com.erdiansyah.mystory.data.Result
import com.erdiansyah.mystory.data.remote.ListStoryItem
import com.erdiansyah.mystory.databinding.FragmentStoryBinding

class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val storyViewModel: StoryViewModel by activityViewModels()
    private val mAdapter = ListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    mAdapter.retry()
                }
            )
            setHasFixedSize(true)
        }
        storyViewModel.getStoryResponse.observe(viewLifecycleOwner) {
            mAdapter.submitData(lifecycle,it)
        }
        mAdapter.setOnItemClickListener(object : ListAdapter.OnItemClickListener{
            override fun onItemClicked(data: ListStoryItem) {
                val toDetailStoryFragment = StoryFragmentDirections.actionStoryFragmentToDetailStoryFragment()
                toDetailStoryFragment.name = data.name
                toDetailStoryFragment.description = data.description
                toDetailStoryFragment.photoUrl = data.photoUrl
                toDetailStoryFragment.createdAt = data.createdAt
                findNavController().navigate(toDetailStoryFragment)
            }
        })

        binding.addStoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_addStoryFragment)
        }
        setHasOptionsMenu(true)

        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//        storyViewModel.getStoryResponse.observe(viewLifecycleOwner) {
//            mAdapter.submitData(lifecycle,it)
////            when (it){
////                is Result.Success -> {
////                    binding.progressBar.visibility = View.GONE
////                    val list = it.data?.listStory ?: emptyList()
////                    mAdapter.setListItem(list)
////                }
////                is Result.Error -> {
////                    Toast.makeText(requireContext(), "Gagal Mendapatkan Story", Toast.LENGTH_SHORT).show()
////                }
////                else -> {
////                    binding.progressBar.visibility = View.VISIBLE
////                }
////            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (requireContext() as AppCompatActivity).menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logoutButton -> {
                viewModel.logoutUser()
                Toast.makeText(requireContext(), "User Logout", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
            R.id.locationStoryButton -> {
                findNavController().navigate(R.id.action_storyFragment_to_mapsStoryFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}