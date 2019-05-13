package com.sysdata.kt.ktandroidarchitecture.ui

import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sysdata.kt.ktandroidarchitecture.R
import com.sysdata.kt.ktandroidarchitecture.repository.model.UIUserLogged
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginActionParams
import com.sysdata.kt.ktandroidarchitecture.viewmodel.LoginViewModel
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.interactor.DataSourceChannel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : FragmentActivity(), View.OnClickListener, TextWatcher {

    private var viewModel : LoginViewModel? = null
    private var adapter: PagedListAdapterImpl? = null

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


        viewModel?.channelNotes?.let {
            if(it is DataSourceChannel<*, *>){
                // TODO: check this code, the cast is unsafe
                it as DataSourceChannel<*, Note>
                val notes = mutableListOf<Note>()
                for(i in 0 until 32){
                    notes.add(Note(i))
                }
                it.initDatasource(notes)
            }
        }

        adapter = PagedListAdapterImpl {
            viewModel?.channelPostNotes?.postData(it)
        }

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        viewModel?.channelNotes?.observe(this,::onPostNote)
        viewModel?.channelPostNotes?.observe(this, ::onReceivePost)
    }

    private fun onReceivePost(note: Note?) {
        Toast.makeText(this, "note : $note", Toast.LENGTH_SHORT).show()
    }

    private fun onPostNote(list: PagedList<Note>?) {
        list?.let {
            adapter?.submitList(list)
        }
    }

    private fun onLoginFailed(failure: Failure?) {
        Toast.makeText(this, "failure : ${failure.toString()}", Toast.LENGTH_SHORT).show()
    }

    private fun onUserLoggged(userLogged: UIUserLogged?) {
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