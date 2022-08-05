package com.cloud.servicemanage.controller.weixin;

import com.cloud.servicemanage.config.MyConfig;
import com.cloud.servicemanage.service.weixin.CoreService;
import com.cloud.servicemanage.utils.weixin.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/core")
public class WxMpPortalController {
  @Autowired
  private MyConfig myConfig;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());






  public String wechatInterface(HttpServletRequest request, HttpServletResponse response) throws Exception{
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String signature= request.getParameter("signature");
    String timestamp= request.getParameter("timestamp");
    String nonce= request.getParameter("nonce");
    String echostr= request.getParameter("echostr");
    this.logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
    if (SignUtil.checkSignature(signature,timestamp, nonce)) {
      return echostr;
    }

    return "非法请求";
  }
  @Autowired
  private CoreService coreService;



  @ResponseBody
  @RequestMapping(value = "",method = RequestMethod.GET, produces = "application/xml; charset=UTF-8")
  public String checkSignature(@RequestParam(name = "signature" ,required = false) String signature  ,
                               @RequestParam(name = "nonce",required = false) String  nonce ,
                               @RequestParam(name = "timestamp",required = false) String  timestamp ,
                               @RequestParam(name = "echostr",required = false) String  echostr) {
//    try {
      /**通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败*/
      if (SignUtil.checkSignature(signature, timestamp, nonce)) {
        logger.info("接入成功");
        return echostr;
      }

      return "非法请求";
//    } catch (RuntimeException e) {
//      logger.error("controller:CoreController. function:checkSignature.. error:" + e.getMessage());
//      logger.error("error:" + e.getMessage());
//      return new JsonResponseData(false, e.getMessage(), 201, null, null).toString();
//    }
  }


  @RequestMapping(value = "",method = RequestMethod.POST, produces = "application/xml; charset=UTF-8")
  public void post(HttpServletRequest req,HttpServletResponse response) throws IOException {
    req.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    try {
      String respMessage = coreService.processRequest(req);
      out.print(respMessage);
      System.out.println(respMessage);
    } catch (RuntimeException e) {
      logger.error("controller:CoreController. function:post.. error:" + e.getMessage());
      logger.error("error:" + e.getMessage());
    }
  }



}
