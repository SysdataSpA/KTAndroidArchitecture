package com.sysdata.kt.ktandroidarchitecture.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.sysdata.kt.ktandroidarchitecture.R
import com.sysdata.kt.ktandroidarchitecture.usecase.GitHubActionParams
import com.sysdata.kt.ktandroidarchitecture.viewmodel.GitHubViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GitHubActivity : AppCompatActivity() {
    private val viewModel by viewModel<GitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_hub)
        viewModel.gitHubAction.observe(this) {
            Log.e("Repo", it.toString())
        }
        viewModel.gitHubAction.execute(GitHubActionParams("SysdataSpA"), viewModel.viewModelScope)
    }
}
