package com.sysdata.kt.ktandroidarchitecture.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.sysdata.kt.ktandroidarchitecture.R
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginActionParams
import com.sysdata.kt.ktandroidarchitecture.viewmodel.LoginViewModel
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : FragmentActivity(), View.OnClickListener, TextWatcher {

    private var viewModel : LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginBtn.setOnClickListener(this)
        loginBtn.isEnabled = validateForm()

        usernameValue.addTextChangedListener(this)
        passwordValue.addTextChangedListener(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        viewModel?.actionLogin?.observe(this, ::onUserLoggged)
        viewModel?.actionLogin?.observeFailure(this, ::onLoginFailed)
    }

    private fun onLoginFailed(failure: Failure?) {
        Toast.makeText(this, "failure : ${failure.toString()}", Toast.LENGTH_SHORT).show()
    }

    private fun onUserLoggged(userLogged: UserLogged?) {
        Toast.makeText(this, "user : ${userLogged?.email}", Toast.LENGTH_SHORT).show()
    }



    override fun onClick(p0: View?) {
        p0?.let {
            viewModel?.actionLogin?.execute(LoginActionParams(usernameValue.text.toString(), passwordValue.text.toString()))
        }
    }

    fun validateForm(): Boolean {
        return usernameValue.text.isNotEmpty() && passwordValue.text.isNotEmpty() && usernameValue.text.isNotBlank() && passwordValue.text.isNotBlank()
    }

    override fun afterTextChanged(p0: Editable?) {
        loginBtn.isEnabled = validateForm()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

}