package com.posite.task.presentation.regist

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.posite.task.R
import com.posite.task.databinding.FragmentRegistBinding
import com.posite.task.presentation.base.BaseFragment
import com.posite.task.presentation.regist.model.UserInfo
import com.posite.task.presentation.regist.vm.RegistUserViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class RegistFragment :
    BaseFragment<FragmentRegistBinding>(R.layout.fragment_regist) {
    private val viewModel: RegistUserViewModelImpl by viewModels<RegistUserViewModelImpl>()
    private var userInfoObserved = false
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

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

    //bitmap으로 바로 넣기
    private val getTakePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            Log.d("bitmap", bitmap.toString())
            bitmap?.let {
                pictureBitmap = it
                binding.profileImageFrame.setImageBitmap(bitmap)
                binding.profileImageFrame.setPadding(0, 0, 0, 0)
            }
        }

    private var pictureUri: Uri? = null
    private var pictureBitmap: Bitmap? = null

    //uri 저장
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            pictureUri?.let {
                //binding.profileImageFrame.setImageURI(pictureUri)
                pictureBitmap = uriToBitmap(pictureUri!!)
                binding.profileImageFrame.setImageBitmap(pictureBitmap)
                binding.profileImageFrame.setPadding(0, 0, 0, 0)
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
        binding.profileImageFrame.clipToOutline = true
        Log.d("name", binding.nameEdit.text.toString())
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
            getTakePicturePreview.launch(null)
            //getTakePicture.launch(pictureUri)
        }
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
}