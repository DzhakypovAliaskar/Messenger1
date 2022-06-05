package com.example.messenger

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.messenger.databinding.FragmentPhotoBinding
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import java.io.File


class PhotoFragment : Fragment() {


    lateinit var binding: FragmentPhotoBinding
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        clickImageOfBanner()
        return binding.root
    }

    private fun clickImageOfBanner() {
        binding.lytGalleryPick.setOnClickListener {
//            if (hasPermissionCheckAndRequest(
//                    permissionLauncher,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//                )
//            )
            showImageProviderDialog()
        }
    }

    private fun showImageProviderDialog() {
        DialogHelper.showChooseAppDialog(
            requireActivity(),
            object : ResultListener<ImageProvider> {
                override fun onResult(t: ImageProvider?) {
                    t?.let {
                        val intent =
                            Intent(
                                requireContext(), ImagePickerActivity::class.java
                            )
                        val bundle = Bundle().apply {
                            putSerializable("extra.image_provider", it)
                            putStringArray("extra.mime_types", emptyArray())
                            putBoolean("extra.crop", false)
                            putFloat("extra.crop_x", 0f)
                            putFloat("extra.crop_y", 0f)
                            putInt("extra.max_width", 0)
                            putInt("extra.max_height", 0)
                            putLong("extra.image_max_size", 0)
                            putString("extra.save_directory", null)
                        }

                        intent.putExtras(bundle)

                        imagePickerContract.launch(
                            intent
                        )
                    }
                }
            },
            object : DismissListener {
                override fun onDismiss() {
                }
            }
        )
    }

    private val imagePickerContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intentFrom = it.data
            if (intentFrom?.data != null) {
                val intent = Intent(requireContext(), UCropActivity::class.java)
                intent.putExtra(UCrop.EXTRA_INPUT_URI, intentFrom.data!!)
                intent.putExtra(
                    UCrop.EXTRA_OUTPUT_URI,
                    Uri.fromFile(
                        File(
                            requireContext().cacheDir,
                            "${System.currentTimeMillis()}.jpg"
                        )
                    )
                )
                val bundle = Bundle()
                bundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_X, 0.3f)
                bundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_Y, 5f)
                intent.putExtras(bundle)
                cropContract.launch(
                    intent
                )
            }
        }

    private val cropContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            val resultUri = it.data?.let { UCrop.getOutput(it) }
            binding.image.loadImage(resultUri.toString(), R.drawable.ic_launcher_background)
            uri = resultUri
        }


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        val intent = Intent(requireContext(), UCropActivity::class.java)
        for (permission in isGranted) {
            when {
                permission.value -> cropContract.launch(intent)
                !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                }
            }
        }
    }
}