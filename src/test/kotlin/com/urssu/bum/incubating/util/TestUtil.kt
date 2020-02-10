package com.urssu.bum.incubating.util

import com.fasterxml.jackson.databind.ObjectMapper

object TestUtil
{
    fun asJsonString(obj: Any): String {
        try {
            return ObjectMapper().writeValueAsString(obj)
        } catch(e: Exception) {
            throw RuntimeException(e)
        }
    }
}