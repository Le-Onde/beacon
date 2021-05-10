package com.shida.eduservice.client;

import org.springframework.stereotype.Component;

@Component
public class OrdersClientVodFileDegradeFeignClient implements OrdersClient{
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
