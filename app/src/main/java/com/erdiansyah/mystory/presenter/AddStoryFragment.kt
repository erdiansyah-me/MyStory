package com.erdiansyah.mystory.presenter

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.erdiansyah.mystory.data.Result
import com.erdiansyah.mystory.databinding.FragmentAddStoryBinding
import com.erdiansyah.mystory.util.createTempFile
import com.erdiansyah.mystory.util.reduceFileImage
import com.erdiansyah.mystory.util.uriToFile
import com.google.android.gms.location.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private val storyViewModel: StoryViewModel by activityViewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var getFile: File? = null

    private lateinit var currentPhotoPath: String

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            getFile = myFile
            binding.imageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedPhoto: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedPhoto, requireContext())
            getFile = myFile
            binding.imageView.setImageURI(selectedPhoto)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        storyViewModel.resetForm()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        storyViewModel.postStoryResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                when(it) {
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data?.message?: "request error", Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error while Upload Story", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadStory() }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    private fun uploadStory(){
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.descTextArea.text.toString().toRequestBody("text/plain".toMediaType())
            val requestPhoto = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val photoMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestPhoto
            )
            if (binding.locationCB.isChecked){
                if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                ){
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val lat = location.latitude
                            val lon = location.longitude
                            storyViewModel.postStory(photoMultiPart, description, lat, lon)
                            Toast.makeText(
                                requireContext(),
                                "Upload with Location",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Location is not found.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                storyViewModel.postStory(photoMultiPart, description, null, null)
                Toast.makeText(
                    requireContext(),
                    "Upload without location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity?.packageManager?.let { intent.resolveActivity(it) }
        activity?.applicationContext?.let { context ->
            createTempFile(context).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.erdiansyah.mystory",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "DIDN'T GET THE PERMISSION.",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}