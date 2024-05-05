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

import cn.rtast.rauth.CLIENT_ID
import cn.rtast.rauth.RAuth
import cn.rtast.rauth.entity.DeviceCodeAccessTokenEntity
import cn.rtast.rauth.entity.DeviceCodeEntity
import cn.rtast.rauth.utils.Http
import okhttp3.FormBody

class DeviceCodeFlow {

    companion object {
        const val DEVICE_CODE_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode"
        const val REDEEM_ACCESS_TOKEN_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token"
    }

    fun getDeviceCode(): DeviceCodeEntity {
        val result = Http.get(DEVICE_CODE_URL, mapOf(
            "client_id" to CLIENT_ID,
            "scope" to "XBoxLive.signin offline_access"
        ))
        return RAuth.gson.fromJson(result, DeviceCodeEntity::class.java)
    }

    fun redeemCode(deviceCode: String): DeviceCodeAccessTokenEntity? {
        val body = FormBody.Builder()
            .add("client_id", CLIENT_ID)
            .add("device_code", deviceCode)
            .add("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
            .build()
        val headers = mapOf(
            "Content-Type" to "application/x-www-form-urlencoded"
        )
        val result = Http.post(REDEEM_ACCESS_TOKEN_URL, mapOf(),body, headers)
        return RAuth.gson.fromJson(result, DeviceCodeAccessTokenEntity::class.java)
    }
}
