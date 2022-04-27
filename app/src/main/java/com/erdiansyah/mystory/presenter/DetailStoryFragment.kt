package com.erdiansyah.mystory.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.erdiansyah.mystory.R
import com.erdiansyah.mystory.databinding.FragmentDetailStoryBinding
import com.erdiansyah.mystory.util.DateFormatter
import java.util.*

class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)

        val dataName = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).name
        val dataDescription = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).description
        val dataPhotoUrl = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).photoUrl
        val dataCreatedAt = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).createdAt

        binding.tvUsername.text = dataName
        binding.datePublish.text = DateFormatter.formatDate(dataCreatedAt, TimeZone.getDefault().id)
        binding.tvDescription.text = dataDescription
        Glide.with(requireContext())
            .load(dataPhotoUrl)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.storyImage)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as AppCompatActivity).supportActionBar?.show()

    }
}