package com.urssu.bum.incubating.unit

import com.urssu.bum.incubating.controller.v1.request.UserSigninRequest
import com.urssu.bum.incubating.controller.v1.request.UserSignupRequest
import com.urssu.bum.incubating.util.TestUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SignupTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `register new user`() {
        val json = TestUtil.asJsonString(UserSignupRequest("name", "password"))

        mvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
    }

    @Test
    fun `conflict error due to duplicate user`() {
        val json = TestUtil.asJsonString(UserSigninRequest("name", "password"))

        mvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

        mvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict())
    }
}