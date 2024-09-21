package Fragment

import Persistence.OpenHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restyle_mobile.R

class LoginFragment : Fragment() {

    lateinit var dbHelper: OpenHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        dbHelper = OpenHelper(requireContext())

        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etPassword = view.findViewById<EditText>(R.id.et_password)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val tvRecoverPassword = view.findViewById<TextView>(R.id.tv_recover_password)
        val tvRegisterPrompt = view.findViewById<TextView>(R.id.tv_register_prompt)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        tvRecoverPassword.setOnClickListener {
            // Navigate to RecoverPasswordFragment
            val recoverFragment = RecoverPasswordFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, recoverFragment)
                .addToBackStack(null)
                .commit()
        }

        tvRegisterPrompt.setOnClickListener {
            val registerFragment = RegisterFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, registerFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
