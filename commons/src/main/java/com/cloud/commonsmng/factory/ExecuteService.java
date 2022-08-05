package com.cloud.commonsmng.factory;

import java.util.Map;

public interface ExecuteService {

    public Map<String,Object> exec(String method, String param) throws Exception;

}
