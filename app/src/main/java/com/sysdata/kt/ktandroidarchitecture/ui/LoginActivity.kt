/**
 * Copyright (C) 2019 Sysdata S.p.a.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

        // initalization of the datasource channel only with the page size because we use a test datasource that don't need initial datas
        viewModel?.channelNotes?.initDatasource(pageSize = 10)

        // init of the adapter with a click listener for the items
        adapter = PagedListAdapterImpl {
            viewModel?.channelPostNotes?.postData(it)
        }

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        // we observe channel notes to have the updates from the adapter that sends the datas through livedata
        viewModel?.channelNotes?.observe(this,::onPostNote)
        // we observe a general channel used to send datas not related to the adapter
        viewModel?.channelPostNotes?.observe(this, ::onReceivePost)
    }

    /**
     * This method recieves the datas from the channel not related to the adapter of the paged list
     * @param note Note?
     */
    private fun onReceivePost(note: Note?) {
        Toast.makeText(this, "note : $note", Toast.LENGTH_SHORT).show()
    }

    /**
     * this method update the adapter with the the list recieved by the livedata
     *
     * @param list PagedList<Note>?
     */
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