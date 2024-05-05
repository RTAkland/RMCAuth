/*
 * Copyright 2024 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package cn.rtast.rauth.utils

import cn.rtast.rauth.RAuth
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URLEncoder

object Http {

    fun buildParamString(params: Map<String, Any>): String {
        if (params.isEmpty()) return ""
        val paramString = StringBuilder("?")
        params.forEach { (key, value) ->
            paramString.append("$key=${URLEncoder.encode(value.toString(), Charsets.UTF_8)}&")
        }.also { paramString.deleteCharAt(paramString.length - 1) }
        return paramString.toString()
    }

    fun get(url: String, params: Map<String, Any>): String {
        val newUrl = url + this.buildParamString(params)
        val request = Request.Builder().url(newUrl).build()
        return RAuth.httpClient.newCall(request).execute().body.string()
    }

    fun post(url: String, params: Map<String, Any>, body: RequestBody, headers: Map<String, Any>): String {
        val newUrl = url + this.buildParamString(params)
        val request = Request.Builder().url(newUrl).post(body)
        headers.forEach { (key, value) ->
            request.addHeader(key, value.toString())
        }
        return RAuth.httpClient.newCall(request.build()).execute().body.string()
    }
}