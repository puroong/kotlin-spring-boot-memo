package com.urssu.bum.incubating.error

class InvalidUserDataError : NotFoundError() {
    override val message = "Invalid User Data"
}