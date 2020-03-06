package fr.isen.berton.projectapp

import com.google.firebase.database.PropertyName


data class Users(
    @PropertyName("id_user") val id_user: String?,
    @PropertyName("nom") val name: String?)



{
    constructor() : this(null,null)
}
