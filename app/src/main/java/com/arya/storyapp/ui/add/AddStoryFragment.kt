package com.arya.storyapp.ui.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arya.storyapp.R
import com.arya.storyapp.data.Result
import com.arya.storyapp.databinding.FragmentAddStoryBinding
import com.arya.storyapp.utils.createCustomTempFile
import com.arya.storyapp.utils.reduceFileImage
import com.arya.storyapp.utils.showSnackbar
import com.arya.storyapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddStoryViewModel by viewModels()

    private lateinit var currentPhotoPath: String
    private var currentFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveSession()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setupTopAppBar()
        setupAction()
        observePostNewStoryResult()
    }

    private fun setupTopAppBar() {
        binding.topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupAction() {
        binding.cameraButton.setOnClickListener { startTakePhoto() }

        binding.galleryButton.setOnClickListener { startGallery() }

        binding.shareLocationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLastLocation()
            } else {
                currentLocation = null
            }
        }

        binding.uploadButton.setOnClickListener {
            uploadStory()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        when {
            it[Manifest.permission.CAMERA] ?: false -> {
                startTakePhoto()
            }
            it[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            else -> {
                binding.root.showSnackbar(
                    "Permission not granted",
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { newLocation: Location? ->
                if (newLocation != null) {
                    currentLocation = newLocation

                    binding.root.showSnackbar(
                        "Location ${currentLocation!!.longitude} ${currentLocation!!.latitude}",
                        Snackbar.LENGTH_SHORT,
                        null
                    ) {}
                } else {
                    binding.root.showSnackbar(
                        "Location is not found. Try again",
                        Snackbar.LENGTH_SHORT,
                        null
                    ) {}

                    binding.shareLocationSwitch.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            currentFile = myFile
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun startTakePhoto() {
        if (checkPermission(Manifest.permission.CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(requireContext().packageManager)

            createCustomTempFile(requireContext()).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.arya.storyapp",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA
                )
            )
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val newFile = uriToFile(selectedImg, requireContext())
            currentFile = newFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadStory() {
        if (currentFile != null) {
            val file = reduceFileImage(currentFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val photo: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val description = binding.descriptionEditText.text.toString()
                .toRequestBody("text/plain".toMediaType())

            var lat: RequestBody? = null
            var lon: RequestBody? = null

            if (currentLocation != null) {
                lat = currentLocation?.latitude.toString()
                    .toRequestBody("text/plain".toMediaType())
                lon = currentLocation?.longitude.toString()
                    .toRequestBody("text/plain".toMediaType())
            }

            viewModel.retrieveSessionResult.observe(viewLifecycleOwner) { session ->
                viewModel.postNewStory(session.token, photo, description, lat, lon)
            }
        } else {
            binding.root.showSnackbar(
                resources.getString(R.string.required_image),
                Snackbar.LENGTH_SHORT,
                null
            ) {}
        }
    }

    private fun observePostNewStoryResult() {
        viewModel.postNewStoryResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.uploadButton.setLoading(true)
                    }
                    is Result.Success -> {
                        binding.uploadButton.setLoading(false)
                        findNavController().navigate(R.id.action_addStoryFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        binding.uploadButton.setLoading(false)
                        binding.root.showSnackbar(
                            result.error,
                            Snackbar.LENGTH_SHORT,
                            null
                        ) {}
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}