package com.ghtk.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    SHOP_READ("shop:read"),
    SHOP_UPDATE("shop:update"),
    SHOP_CREATE("shop:create"),
    SHOP_DELETE("shop:delete"),
    STAFF_READ("staff:read"),
    STAFF_UPDATE("staff:update"),
    STAFF_CREATE("staff:create"),
    STAFF_DELETE("staff:delete");


    @Getter
    private final String permission;
}
