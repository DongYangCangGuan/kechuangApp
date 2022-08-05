package com.cloud.commonsmng.sessionConfig;

import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class sessionConfig {

    public void setSession(HttpServletRequest request, String key,String value)
             throws ServletException, IOException {
        HttpSession session=request.getSession();
        session.setAttribute(key,value);
    }


    public String getSession(HttpServletRequest request, String key)
            throws ServletException, IOException {
        HttpSession session=request.getSession();
       String value= (String)session.getAttribute(key);

       session.removeAttribute(key);
       return value;
    }
}


