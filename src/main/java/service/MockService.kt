package service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileReader
import java.util.*
import javax.servlet.http.HttpServletMapping
import javax.servlet.http.HttpServletRequest

@SpringBootApplication
@RestController
open class MockService {


    @RequestMapping("/mock",method = arrayOf(RequestMethod.GET))
    fun mockService(@RequestBody(required = false) requestBody : String?,
                    @RequestHeader requestHeaders : HttpHeaders,
                    request: HttpServletRequest): ResponseEntity<Any> {
        val requestParameters = request.parameterMap

        println()
        return ResponseEntity(responseBody, responseHeaders, responseStatus)
    }

    companion object {

        var requestBody = String()
        val requestHeaders = HttpHeaders()
        val requestParam = HashMap<String, String>()
        val responseHeaders = HttpHeaders()
        val responseStatus: HttpStatus = HttpStatus.OK
        lateinit var responseBody: String

        @JvmStatic
        fun main(args: Array<String>) {
            loadRequestHeaders()
            loadRequestParams()
            loadRequestBody()
            loadResponseBody()
            SpringApplication.run(MockService::class.java)
        }

        fun loadResponseBody() {
            val reader = FileInputStream("src/main/resources/response/body.txt")
            val fileSize = reader.available()
            val byteArray = ByteArray(fileSize)
            reader.read(byteArray)
            responseBody = String(byteArray)
        }

        fun loadRequestBody() {
            val reader = FileInputStream("src/main/resources/request/body.txt")
            val fileSize = reader.available()
            val byteArray = ByteArray(fileSize)
            reader.read(byteArray)
            requestBody = String(byteArray)
        }

        fun loadRequestHeaders() {
            val getRequestHeadersProperties = Properties()
            getRequestHeadersProperties.load(FileReader("src/main/resources/request/headers.properties"))
            getRequestHeadersProperties.toMap().forEach { key, value ->
                requestHeaders.add(key.toString(), value.toString())
            }
        }

        fun loadRequestParams() {
            val getRequestParamsProperties = Properties()
            getRequestParamsProperties.load(FileReader("src/main/resources/request/params.properties"))
            getRequestParamsProperties.toMap().forEach { key, value ->
                requestParam.put(key.toString(), value.toString())
            }
        }
    }

}