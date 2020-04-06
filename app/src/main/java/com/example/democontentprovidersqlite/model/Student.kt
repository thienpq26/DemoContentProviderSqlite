package com.example.democontentprovidersqlite.model

class Student {
    var mID: Int = 0
    lateinit var mName: String
    lateinit var mPhone: String
    lateinit var mAddress: String
    lateinit var mEmail: String
    lateinit var mImage: ByteArray

    constructor()

    constructor(
        mID: Int,
        mName: String,
        mPhone: String,
        mAddress: String,
        mEmail: String,
        mImage: ByteArray
    ) {
        this.mID = mID
        this.mName = mName
        this.mPhone = mPhone
        this.mAddress = mAddress
        this.mEmail = mEmail
        this.mImage = mImage
    }

    constructor(
        mName: String,
        mPhone: String,
        mAddress: String,
        mEmail: String,
        mImage: ByteArray
    ) {
        this.mName = mName
        this.mPhone = mPhone
        this.mAddress = mAddress
        this.mEmail = mEmail
        this.mImage = mImage
    }
}
