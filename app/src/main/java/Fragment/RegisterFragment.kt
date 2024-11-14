package Fragment

import Persistence.UserHelper
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restyle_mobile.Interface.RetrofitClient
import com.example.restyle_mobile.Interface.UploadResponse
import com.example.restyle_mobile.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterFragment : Fragment() {

    private lateinit var dbHelper: UserHelper
    private var photoUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        dbHelper = UserHelper(requireContext())

        val etFullName = view.findViewById<EditText>(R.id.et_full_name)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etPassword = view.findViewById<EditText>(R.id.et_password)
        val etConfirmPassword = view.findViewById<EditText>(R.id.et_confirm_password)
        val cbIsRemodeler = view.findViewById<CheckBox>(R.id.cb_is_remodeler)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val btnSelectPhoto = view.findViewById<Button>(R.id.btn_upload_photo)
        val ivSelectedPhoto = view.findViewById<ImageView>(R.id.iv_profile_photo)
        val tvAlreadyHaveAccount = view.findViewById<TextView>(R.id.tv_already_have_account)

        val checkbox = view.findViewById<CheckBox>(R.id.cb_terms_conditions)
        val termsText = "Acepto los términos y condiciones"
        val spannable = SpannableString(termsText)

        val termsStart = termsText.indexOf("términos y condiciones")
        val termsEnd = termsText.length

        val termsClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val url = "https://mondongodev.github.io/restyle-landing-page/terms.html"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                widget.context.startActivity(intent)
            }
        }

        spannable.setSpan(termsClickableSpan, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        checkbox.text = spannable
        checkbox.movementMethod = LinkMovementMethod.getInstance()

        btnSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val isRemodeler = cbIsRemodeler.isChecked

            if (photoUri == null) {
                Toast.makeText(requireContext(), "Please select a photo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Handle the image URI
            val file = getFileFromUri(photoUri!!)
            if (file != null) {
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val authHeader = "600f35d5ce72069"

                // Call Retrofit to upload the image
                RetrofitClient.uploadImage(authHeader, body).enqueue(object : Callback<UploadResponse> {
                    override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val photoUrl = response.body()!!.data.link
                            // Now save the photo URL to the database
                            val success = dbHelper.addUser(fullName, email, password, isRemodeler, photoUrl)

                            if (success) {
                                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()

                                // Navigate to LoginFragment after successful registration
                                val loginFragment = LoginFragment()
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, loginFragment)
                                    .addToBackStack(null)
                                    .commit()
                            } else {
                                Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "Image upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(requireContext(), "Failed to get image file", Toast.LENGTH_SHORT).show()
            }
        }


        tvAlreadyHaveAccount.setOnClickListener {
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun getFileFromUri(uri: Uri): File? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "uploaded_image.jpg")
        val outputStream = file.outputStream()
        inputStream?.copyTo(outputStream)
        return file.takeIf { it.exists() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            photoUri = data?.data
            view?.findViewById<ImageView>(R.id.iv_profile_photo)?.setImageURI(photoUri)
        }
    }
}
