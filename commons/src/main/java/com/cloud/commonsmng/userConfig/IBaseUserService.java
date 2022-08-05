package com.cloud.commonsmng.userConfig;

import com.cloud.commonsmng.entity.BaseUserInfo;
import org.springframework.stereotype.Service;

public interface IBaseUserService {
     BaseUserInfo getUserInfo(String ID);
}
