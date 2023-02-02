package com.example.githubapp

class RepoModel {
    var repo_name: String? = null
    var desc: String? = null
    var url: String? = null



    fun getRepoNames(): String {
        return repo_name.toString()
    }

    fun setRepoNames(repo_name: String) {
        this.repo_name = repo_name
    }

    fun getDescs(): String {
        return desc.toString()
    }

    fun setDescs(desc: String) {
        this.desc = desc
    }

    fun getUrls(): String {
        return url.toString()
    }

    fun setUrls(url: String) {
        this.url = url
    }


}