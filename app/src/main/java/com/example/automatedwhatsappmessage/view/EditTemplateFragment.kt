package com.example.automatedwhatsappmessage.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.Common.showDialog
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.ViewModel.TemplateListViewModel
import com.example.automatedwhatsappmessage.api.AddTemplateBody
import com.example.automatedwhatsappmessage.api.ApiStatus
import com.example.automatedwhatsappmessage.databinding.FragmentEditTemplateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditTemplateFragment : Fragment() {

    private val GALLERY = 1
    private var CAMERA: Int = 2
    private val RESULT_CANCELED: Any? = 0
    private var _binding: FragmentEditTemplateBinding? = null
    private val binding get() = _binding!!

    private var templateType = ""
    private var imagePath = ""
     var imagebitmap:Bitmap? = null
    private val templateListViewModel: TemplateListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditTemplateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editAddTemplateName.isEnabled = false
        binding.editAddContent.isEnabled = false
        binding.editAddLocation.isEnabled = false
        binding.spinnerTemplateType.isEnabled = false
        binding.spinnerTemplateType.visibility = View.INVISIBLE
        binding.imageAddImage.isEnabled = false
        binding.textSpinnervalue.visibility = View.VISIBLE
        binding.buttonCancel.visibility = View.GONE
        binding.buttonSave.visibility = View.GONE


        editValue()
        spinner()


        binding.imageAddImage.setOnClickListener {

            showPictureDialog()
        }

        binding.txtvEdit.setOnClickListener{
            binding.editAddTemplateName.isEnabled = true
            binding.editAddContent.isEnabled = true
            binding.editAddLocation.isEnabled = true
            binding.spinnerTemplateType.isEnabled = true
            binding.imageAddImage.isEnabled = true
            binding.textSpinnervalue.visibility = View.INVISIBLE
            binding.spinnerTemplateType.visibility = View.VISIBLE
            binding.buttonCancel.visibility = View.VISIBLE
            binding.buttonSave.visibility = View.VISIBLE

        }

        binding.buttonSave.setOnClickListener {
            if (imagebitmap!=null){
                val savedFile  =  saveBitmapToFile(requireContext(),imagebitmap!!,"SendImage.jpg")
                if (savedFile != null){
                    imagePath = savedFile.absolutePath
                    Log.d(TAG, "image:${AppPreferences.image_path}")
                    Log.d(TAG, "The gallery image was successfully saved to the file")
                    Log.d(TAG, "imagepath${savedFile}")
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    binding.imageAddImage.setImageBitmap(bitmap)
                    // The image was successfully saved to the file.
                    // You can now use the 'savedFile' as needed.
                } else {
                    Log.d(TAG, "There was an error saving the image to the file")
                    // There was an error saving the image to the file.
                }
            }

            templateListViewModel.editTemplateList(AddTemplateBody(
                loginMobileNumber = AppPreferences.phoneNumber,
                messageTemplateId =  AppPreferences.messageTemplateId,
                templateName =   binding.editAddTemplateName.text.toString(),
                templateContent =  binding.editAddContent.text.toString(),
                templateLocation =  binding.editAddLocation.text.toString(),
                templateImage = imagePath,
                templateType = templateType,
            ))

            templateListViewModel.editTemplateListLiveData.observe(viewLifecycleOwner){
                    res ->
                res.let {
                        resource ->
                    when(resource.status){
                        ApiStatus.SUCCESS ->{
                            Log.d(TAG, "onViewCreated: ")
                            lifecycleScope.launch(Dispatchers.Main) {
                                findNavController().navigate(R.id.action_editTemplateFragment_to_templateFragment)
                                Toast.makeText(requireContext(), "succesfull", Toast.LENGTH_SHORT).show()
                            }

                        }
                        ApiStatus.ERROR -> {
                            when (resource.code) {
                                404 -> {
                                    showDialog(
                                        "${res.message}",
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                                503 -> {
                                    showDialog(
                                        "${res.message}",
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                                500 -> {
                                    showDialog(
                                        res.message.toString(),
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                                else -> {
                                    showDialog(
                                        resource.message.toString(),
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                            }

                            Log.d(SignInFragment.TAG, "response:${resource.code}")
                            Log.d(SignInFragment.TAG, "responsemessage:${resource.message}")
                        }
                        ApiStatus.LOADING ->{}
                    }
                }
            }

        }

        binding.buttonCancel.setOnClickListener {
            templateType = ""
            binding.editAddTemplateName.text.clear()
            binding.editAddContent.text.clear()
            binding.editAddTemplateName.text.clear()
            binding.editAddLocation.text.clear()
            binding.imageAddImage.setImageBitmap(null)
            binding.spinnerTemplateType.isEnabled = false
            binding.editAddLocation.isFocusable = false
            binding.editAddContent.isFocusable = false
            binding.editAddTemplateName.isFocusable = false
            binding.buttonCancel.visibility = View.GONE
            binding.buttonSave.visibility = View.GONE
            binding.spinnerTemplateType.visibility = View.INVISIBLE
            binding.textSpinnervalue.visibility = View.VISIBLE
            editValue()
        }



    }

    fun editValue(){
        if (AppPreferences.templateType?.isNotEmpty() == true) binding.textSpinnervalue.text = AppPreferences.templateType.toString()
        if (AppPreferences.templateContent?.isNotEmpty() == true) binding.editAddContent.setText(AppPreferences.templateContent.toString())
        if (AppPreferences.templateName?.isNotEmpty() == true) binding.editAddTemplateName.setText(AppPreferences.templateName.toString())
        if (AppPreferences.templateLocation?.isNotEmpty() == true) binding.editAddLocation.setText(AppPreferences.templateLocation.toString())
        if (AppPreferences.templateImage?.isNotEmpty() == true) {
            val bitmap = BitmapFactory.decodeFile(AppPreferences.templateImage)
            binding.imageAddImage.setImageBitmap(bitmap)
        }
    }

    private fun spinner(){
        var type = resources.getStringArray(R.array.TemplateType)

        if (binding.spinnerTemplateType != null) {
            if(AppPreferences.templateType?.isNotEmpty() == true) templateType = AppPreferences.templateType.toString()
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, type)
            binding.spinnerTemplateType.adapter = adapter
            // on below line we are getting the position of the item by the item name in our adapter.
            val spinnerPosition: Int = adapter.getPosition(templateType)

            // on below line we are setting selection for our spinner to spinner position.
            binding.spinnerTemplateType.setSelection(spinnerPosition)


            binding.spinnerTemplateType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(requireContext(), getString(R.string.selected_item) + " " + "" + type[position], Toast.LENGTH_SHORT).show()
                    // templatetype = getString(R.string.selected_item)
                    val text: String = parent?.getItemAtPosition(position).toString()
                    templateType = text
                    binding.textSpinnervalue.text = text
                    Log.d(TAG, "onItemSelected: ${type}")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

    }


    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { _dialog, which ->
                when (which){
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            })
        pictureDialog.show()
    }


    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == this.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI: Uri? = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, contentURI)
                    //val path: String = saveImage(bitmap)
                    Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
                    //ImageUpload(bitmap)
                    imagebitmap = bitmap
                    binding.imageAddImage.setImageBitmap(imagebitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!["data"] as Bitmap?
            imagebitmap = thumbnail
            binding.imageAddImage.setImageBitmap(thumbnail)
        }

    }


    // Function to save bitmap image to a file

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String): File? {
        // Get the directory to save the image
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "sendImage")

        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Create the file object
        val file = File(directory, getUniqueFilename(filename))

        try {
            // Create a file output stream
            val outputStream: OutputStream = FileOutputStream(file)

            // Compress the bitmap to JPEG format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            // Flush and close the stream
            outputStream.flush()
            outputStream.close()

            // Return the file
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return null if there was an error
        return null
    }

    private fun getUniqueFilename(filename: String): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val random = (0..999).random() // Generate a random number between 0 and 999
        return "$filename$timeStamp$random.jpg"
    }

companion object{
    const val TAG = "EditTemplateFragment"
}



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}