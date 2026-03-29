package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Test

internal class ErrorResponseInterceptorTest {

    private val interceptor = ErrorResponseInterceptor()

    @Test
    fun `intercept should return response when request is successful`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns true

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `intercept should throw UnauthorizedException for 401 status`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBody = mockk<ResponseBody>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns false
        every { response.code } returns 401
        every { response.body } returns responseBody
        every { responseBody.string() } returns "Unauthorized"

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected UnauthorizedException to be thrown")
        } catch (e: UnauthorizedException) {
            assertThat(e.code).isEqualTo(401)
            assertThat(e.errorBody).isEqualTo("Unauthorized")
        }
    }

    @Test
    fun `intercept should throw ForbiddenException for 403 status`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBody = mockk<ResponseBody>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns false
        every { response.code } returns 403
        every { response.body } returns responseBody
        every { responseBody.string() } returns "Forbidden"

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected ForbiddenException to be thrown")
        } catch (e: ForbiddenException) {
            assertThat(e.code).isEqualTo(403)
            assertThat(e.errorBody).isEqualTo("Forbidden")
        }
    }

    @Test
    fun `intercept should throw NotFoundException for 404 status`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBody = mockk<ResponseBody>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns false
        every { response.code } returns 404
        every { response.body } returns responseBody
        every { responseBody.string() } returns "Not Found"

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected NotFoundException to be thrown")
        } catch (e: NotFoundException) {
            assertThat(e.code).isEqualTo(404)
            assertThat(e.errorBody).isEqualTo("Not Found")
        }
    }

    @Test
    fun `intercept should throw ServerException for 500 status`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBody = mockk<ResponseBody>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns false
        every { response.code } returns 500
        every { response.body } returns responseBody
        every { responseBody.string() } returns "Internal Server Error"

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected ServerException to be thrown")
        } catch (e: ServerException) {
            assertThat(e.code).isEqualTo(500)
            assertThat(e.errorBody).isEqualTo("Internal Server Error")
        }
    }

    @Test
    fun `intercept should throw HttpException for other error codes`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBody = mockk<ResponseBody>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns false
        every { response.code } returns 418
        every { response.body } returns responseBody
        every { responseBody.string() } returns "I'm a teapot"

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected HttpException to be thrown")
        } catch (e: HttpException) {
            assertThat(e.code).isEqualTo(418)
            assertThat(e.errorBody).isEqualTo("I'm a teapot")
        }
    }
}
