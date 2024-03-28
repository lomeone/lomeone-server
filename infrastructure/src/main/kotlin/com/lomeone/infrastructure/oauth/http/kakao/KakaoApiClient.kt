package com.lomeone.infrastructure.oauth.http.kakao

import org.springframework.cloud.openfeign.FeignClient


@FeignClient(name = "kakao-api", url = "\${oauth.kakao.url.api}")
interface KakaoApiClient {
}
