package Fragment

import Persistence.UserHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restyle_mobile.R

class RegisterFragment : Fragment() {

    lateinit var dbHelper: UserHelper

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
        val tvAlreadyHaveAccount = view.findViewById<TextView>(R.id.tv_already_have_account)

        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val isRemodeler = cbIsRemodeler.isChecked

            if (password == confirmPassword) {
                val success = dbHelper.addUser(fullName, email, password, isRemodeler)
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
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to LoginFragment when text is clicked
        tvAlreadyHaveAccount.setOnClickListener {
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
