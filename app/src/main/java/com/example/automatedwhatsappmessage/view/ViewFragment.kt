package com.example.automatedwhatsappmessage.view

import android.Manifest.permission.CAMERA
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
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.Common.showDialog
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.ViewModel.TemplateListViewModel
import com.example.automatedwhatsappmessage.api.AddTemplateBody
import com.example.automatedwhatsappmessage.api.ApiStatus
import com.example.automatedwhatsappmessage.databinding.FragmentViewBinding
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

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ViewFragment : Fragment() {

    private val GALLERY = 1
    private var CAMERA: Int = 2
    private val RESULT_CANCELED: Any? = 0
    private val IMAGE_DIRECTORY = "/demonuts"
    private var _binding:FragmentViewBinding? = null
    private val binding get() = _binding!!
    private var templatetype:String? = null
    private val templateListViemodel: TemplateListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var type = resources.getStringArray(R.array.TemplateType)




        // access the spinner
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, type)
        binding.spinnerTemplateType.adapter = adapter

        binding.spinnerTemplateType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                Toast.makeText(requireContext(), getString(R.string.selected_item) + " " + "" + type[position], Toast.LENGTH_SHORT).show()
               // templatetype = getString(R.string.selected_item)
                val text: String = parent?.getItemAtPosition(position).toString()
                templatetype = text

                Log.d(TAG, "onItemSelected: ${type}")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        binding.imageAddImage.setOnClickListener {

            showPictureDialog()
        }


        binding.buttonAdd.setOnClickListener {
            if (binding.editAddTemplateName.text.isEmpty()){
                binding.editAddTemplateName.error = "Template name required"
                binding.editAddTemplateName.requestFocus()
                return@setOnClickListener
            }
            if (binding.editAddContent.text.isEmpty()){
                binding.editAddContent.error = "Template content required"
                binding.editAddContent.requestFocus()
                return@setOnClickListener
            }

            templateListViemodel.addTemplateList(AddTemplateBody(
                loginMobileNumber = AppPreferences.phoneNumber.toString(),
                messageTemplateId = "",
                templateName = binding.editAddTemplateName.text.toString(),
                templateContent = binding.editAddContent.text.toString(),
                templateType = templatetype,
                templateLocation = binding.editAddLocation.text.toString(),
                templateImage = AppPreferences.image_path
            ))
        }


        templateListViemodel.addTemplateListLiveData.observe(viewLifecycleOwner){
            res ->
            res.let {
                resource ->
                when(resource.status){
                    ApiStatus.SUCCESS ->{
                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "succesfull", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_viewFragment_to_templateFragment)
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
                    val savedFile  =  saveBitmapToFile(requireContext(),bitmap!!,"SendImage.jpg")
                    if (savedFile != null){
                        val  imagePath = savedFile.absolutePath
                        AppPreferences.image_path = savedFile.absolutePath.toString()
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


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!["data"] as Bitmap?
            val savedFile  =  saveBitmapToFile(requireContext(),thumbnail!!,"SendImage.jpg")
            if (savedFile != null){
              val  imagePath = savedFile.absolutePath
                AppPreferences.image_path = savedFile.absolutePath.toString()
                Log.d(TAG, "image:${AppPreferences.image_path}")
                Log.d(TAG, "The image was successfully saved to the file")
                Log.d(TAG, "imagepath${savedFile}")
                val bitmap = BitmapFactory.decodeFile(imagePath)
                binding.imageAddImage.setImageBitmap(bitmap)

                // The image was successfully saved to the file.
                // You can now use the 'savedFile' as needed.
            } else {
                Log.d(TAG, "There was an error saving the image to the file")
                // There was an error saving the image to the file.
            }

          //  ImageUpload(thumbnail)
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





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val TAG = "ViewFragment"
    }


}