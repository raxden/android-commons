package com.raxdenstudios.commons.network

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.network.ext.errorBodyOrNull
import com.raxdenstudios.commons.network.ext.parseError
import com.raxdenstudios.commons.network.ext.retrofitOrNull
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

internal class RetrofitExceptionExtensionKtTest {

    private val errorBodyMock = mockk<ResponseBody>()
    private val responseMock = mockk<Response<String>> {
        every { errorBody() } returns errorBodyMock
    }
    private val converterMock = mockk<Converter<ResponseBody, String>> {
        every { convert(errorBodyMock) } returns "error"
    }
    private val retrofitMock = mockk<Retrofit> {
        every { responseBodyConverter<String>(any(), any()) } returns converterMock
    }
    private val retrofitClientExceptionMock = mockk<RetrofitException.Non200Http.Client> {
        every { retrofit } returns retrofitMock
        every { response } returns responseMock
    }
    private val retrofitNetworkExceptionMock = mockk<RetrofitException.Network>()
    private val retrofitUnexpectedExceptionMock = mockk<RetrofitException.Unexpected>()

    @Test
    fun `parseError should be return an error`() {
        val result = retrofitClientExceptionMock.parseError<String>()

        assertThat(result).isEqualTo("error")
    }

    @Test
    fun `errorBodyOrNull should be return an response`() {
        val result = retrofitClientExceptionMock.errorBodyOrNull()

        assertThat(result).isNotNull()
    }

    @Test
    fun `errorBodyOrNull should be return a null when is a network exception`() {
        val result = retrofitNetworkExceptionMock.errorBodyOrNull()

        assertThat(result).isNull()
    }

    @Test
    fun `errorBodyOrNull should be return a null when is a unexpected exception`() {
        val result = retrofitUnexpectedExceptionMock.errorBodyOrNull()

        assertThat(result).isNull()
    }

    @Test
    fun `retrofitOrNull should be return an response`() {
        val result = retrofitClientExceptionMock.retrofitOrNull()

        assertThat(result).isInstanceOf(Retrofit::class.java)
    }

    @Test
    fun `retrofitOrNull should be return a null when is a network exception`() {
        val result = retrofitNetworkExceptionMock.retrofitOrNull()

        assertThat(result).isNull()
    }

    @Test
    fun `retrofitOrNull should be return a null when is a unexpected exception`() {
        val result = retrofitUnexpectedExceptionMock.retrofitOrNull()

        assertThat(result).isNull()
    }
}
