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


package cn.rtast.rauth.api

import cn.rtast.rauth.RAuth
import cn.rtast.rauth.entity.XBoxLiveAuthEntity
import cn.rtast.rauth.entity.model.XBoxLiveAuthModel
import cn.rtast.rauth.entity.model.XSTSTokenModel
import cn.rtast.rauth.utils.Http
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class XboxLive {

    companion object {
        const val XBOXLIVE_AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate"
        const val XSTS_URL = "https://xsts.auth.xboxlive.com/xsts/authorize"
    }

    fun authXBoxLive(accessToken: String): XBoxLiveAuthEntity {
        val xboxLiveObj = XBoxLiveAuthModel(
            XBoxLiveAuthModel.Properties("RPS", "d=$accessToken", "user.auth.xboxlive.com"),
            "http://auth.xboxlive.com",
            "JWT"
        )
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json"
        )
        val xboxLiveObjBody = RAuth.gson.toJson(xboxLiveObj).toRequestBody("application/json".toMediaType())
        val result = Http.post(XBOXLIVE_AUTH_URL, mapOf(), xboxLiveObjBody, headers)
        return RAuth.gson.fromJson(result, XBoxLiveAuthEntity::class.java)
    }

    fun xstsToken(xblToken: String): XBoxLiveAuthEntity {
        val xstsObj = XSTSTokenModel(
            XSTSTokenModel.Properties("RETAIL", listOf(xblToken)),
            "rp://api.minecraftservices.com/",
            "JWT"
        )
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json"
        )
        val xstsObjBody = RAuth.gson.toJson(xstsObj).toRequestBody("application/json".toMediaType())
        val result = Http.post(XSTS_URL, mapOf(), xstsObjBody, headers)
        return RAuth.gson.fromJson(result, XBoxLiveAuthEntity::class.java)
    }
}
