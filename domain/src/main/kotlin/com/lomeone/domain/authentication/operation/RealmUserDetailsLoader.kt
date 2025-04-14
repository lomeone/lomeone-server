package com.lomeone.domain.authentication.operation

import org.springframework.security.core.userdetails.UserDetails

typealias RealmUserDetailsLoader = (realmCode: String, username: String) -> UserDetails
