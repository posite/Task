package com.posite.task.presentation.regist

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.MediaStore
import android.text.InputFilter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.posite.task.R
import com.posite.task.databinding.FragmentRegistBinding
import com.posite.task.presentation.base.BaseFragment
import com.posite.task.presentation.regist.model.UserInfo
import com.posite.task.presentation.regist.vm.RegistUserViewModelImpl
import com.posite.task.util.BitmapConverter.convertToGrayscale
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.regex.Pattern

@AndroidEntryPoint
class RegistFragment :
    BaseFragment<FragmentRegistBinding>(R.layout.fragment_regist) {
    private val viewModel: RegistUserViewModelImpl by viewModels<RegistUserViewModelImpl>()
    private var userInfoObserved = false
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var pictureUri: Uri? = null
    private var pictureBitmap: Bitmap? = null
    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
                if (!it.value) {
                    //Toast.makeText(requireContext(), "권한 허용 필요", Toast.LENGTH_SHORT).show()
                }
            }
        }


    override fun onResume() {
        super.onResume()
        pictureBitmap?.let {
            binding.profileImageFrame.setImageBitmap(it)
            binding.profileImageFrame.setPadding(0, 0, 0, 0)
        }
    }

    override fun initObserver() {

    }

    override fun initView() {
        binding.nameEdit.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile(getString(R.string.input_format))
            if (!ps.matcher(source).matches()) {
                ""
            } else source
        })
        outputDirectory = getOutputDirectory()
        startCamera()
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.profileImageFrame.clipToOutline = true
        requestMultiplePermission.launch(permissionList)
        binding.viewModel = viewModel
        binding.birthdayEdit.setOnClickListener {
            chooseBirthday()
        }

        binding.registBtn.setOnClickListener {
            userInfoObserved = false
            //viewModel.registBtnClick()
            val name = binding.nameEdit.text.toString()
            val birthday = binding.birthdayEdit.text.toString()
            if (name.isNotBlank() && birthday.isNotBlank() && pictureBitmap != null) {
                val action =
                    RegistFragmentDirections.actionRegistFragmentToFinishRegistFragment(
                        UserInfo(name, birthday, pictureBitmap!!)
                    )
                this@RegistFragment.findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.profileImageFrame.setOnClickListener {
            pictureUri = createImageFile()
            takePhoto()
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    exc.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    val img =
                        convertToGrayscale(uriToBitmap(savedUri)!!).rotate(270f).reverse()
                    pictureBitmap = img
                    binding.profileImageFrame.setImageBitmap(img)
                    binding.profileImageFrame.setPadding(0, 0, 0, 0)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()



            imageCapture = ImageCapture.Builder()
                .setCameraSelector(CameraSelector.DEFAULT_FRONT_CAMERA)
                .build()

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, CameraSelector.DEFAULT_FRONT_CAMERA, imageCapture
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun chooseBirthday() {
        val dialog = DatePickerDialog(
            requireContext(),
            R.style.MySpinnerDatePickerStyle,
            { _, cYear, cMonth, cDay ->
                run {
                    if (cMonth < 10 && cDay < 10) {
                        binding.birthdayEdit.text =
                            getString(
                                R.string.birthday_format,
                                cYear,
                                "0${cMonth + 1}",
                                "0$cDay"
                            )
                    } else if (cMonth < 10) {
                        binding.birthdayEdit.text =
                            getString(
                                R.string.birthday_format,
                                cYear,
                                "0${cMonth + 1}",
                                cDay.toString()
                            )
                    } else if (cDay < 10) {
                        binding.birthdayEdit.text =
                            getString(
                                R.string.birthday_format,
                                cYear,
                                (cMonth + 1).toString(),
                                "0$cDay"
                            )
                    } else {
                        binding.birthdayEdit.text =
                            getString(
                                R.string.birthday_format,
                                cYear,
                                (cMonth + 1).toString(),
                                cDay.toString()
                            )
                    }
                    year = cYear
                    month = cMonth
                    day = cDay
                }
            },
            year,
            month,
            day
        )
        val color = ContextCompat.getColor(requireActivity(), R.color.highlight)
        dialog.show()
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(color)
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(color)
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            content
        )
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // fragment가 detach되면 observe를 중단하도록 합니다.
        userInfoObserved = true
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private fun Bitmap.rotate(degrees: Float): Bitmap =
            Bitmap.createBitmap(
                this,
                0,
                0,
                width,
                height,
                Matrix().apply { postRotate(degrees) },
                true
            )

        private fun Bitmap.reverse(): Bitmap =
            Bitmap.createBitmap(
                this,
                0,
                0,
                width,
                height,
                Matrix().apply { setScale(-1f, 1f) },
                false
            )
    }

}