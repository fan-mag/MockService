package service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.FileInputStream
import java.util.*
import javax.servlet.http.HttpServletRequest

@SpringBootApplication
@RestController
open class MockService {

    @RequestMapping("/", method = arrayOf(RequestMethod.GET))
    fun mockService(@RequestBody(required = false) requestBody: String?,
                    @RequestHeader requestHeaders: HttpHeaders,
                    request: HttpServletRequest): ResponseEntity<Any> {
        val requestParameters = request.parameterMap
        loadResponse()
        /* Mock Logic Start */
        println("BODY: $requestBody")
        requestHeaders.forEach { key, value -> println("HEADER: $key : $value") }
        requestParameters.forEach { key, value -> println("PARAMS: $key : $value") }
        /* Mock Logic End */
        return ResponseEntity(responseBody, responseHeaders, responseStatus)
    }

    companion object {
        var responseHeaders = HttpHeaders()
        var responseStatus = HttpStatus.OK
        var responseBody = String()

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(MockService::class.java)
        }

        fun loadResponse() {
            responseStatus = loadResponseStatus()
            responseHeaders = loadResponseHeaders()
            responseBody = loadResponseBody()
        }

        fun loadResponseBody(): String {
            val reader = FileInputStream("src/main/resources/response-body.txt")
            val fileSize = reader.available()
            val byteArray = ByteArray(fileSize)
            reader.read(byteArray)
            return String(byteArray)
        }

        fun loadResponseHeaders(): HttpHeaders {
            val properties = Properties()
            properties.load(FileInputStream("src/main/resources/response-headers.properties"))
            val headers = HttpHeaders()
            properties.toMap().forEach { key, value ->
                headers.add(key.toString(), value.toString())
            }
            return headers
        }

        fun loadResponseStatus(): HttpStatus {
            val reader = FileInputStream("src/main/resources/response-statuscode.txt")
            val fileSize = reader.available()
            val byteArray = ByteArray(fileSize)
            reader.read(byteArray)
            val statusCode: Int = String(byteArray).toInt()
            return HttpStatus.valueOf(statusCode)
        }

    }

}