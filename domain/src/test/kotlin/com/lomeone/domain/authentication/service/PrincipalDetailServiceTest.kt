package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.Role
import com.lomeone.domain.user.entity.RoleName
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.entity.UserRole
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class PrincipalDetailServiceTest : BehaviorSpec({
    val authenticationRepository: AuthenticationRepository = mockk()
    val principalDetailService = PrincipalDetailService(authenticationRepository)

    val testUid = "testUid"
    val emailInput = "test@gmail.com"
    val mockUser: User = mockk()

    Given("이메일 인증정보가 있으면") {
        every { mockUser.userRoles } returns listOf(
            UserRole(
                role = Role(
                    roleName = RoleName.MEMBER
                )
            )
        )
        every { authenticationRepository.findByEmailAndProvider(email = any(), provider = AuthProvider.EMAIL)} returns
            Authentication(
                uid = testUid,
                email = Email(emailInput),
                password = "testPassword1324@",
                provider = AuthProvider.EMAIL,
                user = mockUser
            )

        When("loadUser를 할 때") {
            val userDetails = principalDetailService.loadUserByUsername(emailInput)

            Then("인증 정보를 가져온다") {
                userDetails.authorities.map { it.authority } shouldContain "ROLE_MEMBER"
                userDetails.password shouldBe "testPassword1324@"
                userDetails.username shouldBe testUid
                userDetails.isAccountNonExpired shouldBe true
                userDetails.isAccountNonLocked shouldBe true
                userDetails.isCredentialsNonExpired shouldBe true
                userDetails.isEnabled shouldBe true
            }
        }
    }

    Given("이메일 인증 정보가 없으면") {
        every { authenticationRepository.findByEmailAndProvider(email = any(), provider = AuthProvider.EMAIL) } returns null

        When("loadUser를 할 때") {
            Then("인증 정보가 없다는 예외가 발생해서 인증정보를 가져올 수 없다") {
                shouldThrow<Exception> {
                    principalDetailService.loadUserByUsername(emailInput)
                }
            }
        }
    }
})
