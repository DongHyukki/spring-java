package com.donghyukki.springjava.support.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
public @interface AllowedUserOrAdmin {
}
