package com.shida.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean send(Map<String, Object> param, String phone);
}
