package com.urssu.bum.incubating.config

import com.urssu.bum.incubating.filter.JwtRequestFilter
import com.urssu.bum.incubating.service.MyUserDeatilsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class WebSecurityConfig @Autowired constructor(
        private var myUserDetailsService: MyUserDeatilsService,
        private val jwtRequestFilter: JwtRequestFilter
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers("/*/signin").permitAll()
                    .antMatchers("/*/signup").permitAll()
                    .anyRequest().authenticated()
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)

    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}