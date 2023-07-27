package com.ghtk.request.order_manage;

import java.util.Map;

public class UpdateOrderRequest {
    public String order_id;
    public Map<String, OrderItem> product_ids;
}
