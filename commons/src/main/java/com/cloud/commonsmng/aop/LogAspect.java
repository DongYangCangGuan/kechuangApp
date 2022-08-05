package com.cloud.commonsmng.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.entity.Loginfo;
import com.cloud.commonsmng.kafkaUtils.KafkaMessager;
import com.cloud.commonsmng.security.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

//    @Autowired
//    KafkaMessager kafkaMessager;

    @Autowired
    JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

   // private static final Loginfo operationLog = new Loginfo();

    ThreadLocal<Loginfo> operationLog=new ThreadLocal<>();

    @Autowired
//    RedisTemplate redisTemplate;
    //切入点
    @Pointcut("@annotation(com.cloud.commonsmng.aop.OperationLogDetail)")
    public void operationLog(){}

    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        operationLog.set(new Loginfo());
        long time = System.currentTimeMillis();
     //   HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


        try {
            res =  joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            return res;
        } finally {
            try {
                //方法执行完成后增加日志
                addOperationLog(joinPoint,res,time);
            }catch (Exception e){
               logger.error("LogAspect 操作失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addOperationLog(JoinPoint joinPoint, Object res, long time){
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();


        Object[] args=joinPoint.getArgs();



        Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                continue;
            }
            arguments[i] = args[i];
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String userid="";
        String token= request.getHeader("token");
        if(token!=null && !token.equals("")){
            Map<String, String> userinfo = jwtUtil.verifyToken(token);
             userid=userinfo.get("userid");
        }
        String url=request.getRequestURI().toLowerCase();

        String paramter = "";
        if (arguments != null) {
            try {
                paramter = JSONObject.toJSONString(arguments);

            } catch (Exception e) {
                paramter = arguments.toString();
            }
        }

        operationLog.get().setRUNTIME(time+"");
        operationLog.get().setLOG_LEVEL("info");
        operationLog.get().setOUTPUTPARAM(JSON.toJSONString(res));
        operationLog.get().setACTIONTYPE(url.contains("auth")?"0":"1");
        operationLog.get().setINPUTPARAM(paramter);
        operationLog.get().setCLASS(JSON.toJSONString(joinPoint.getClass()));
        operationLog.get().setFUNCTION(signature.getDeclaringTypeName() + "." + signature.getName());
        operationLog.get().setUSERID(userid);
        OperationLogDetail annotation = signature.getMethod().getAnnotation(OperationLogDetail.class);


        if(annotation!=null) {
            operationLog.get().setDESCRIB(getDetail(((MethodSignature) joinPoint.getSignature()).getParameterNames(), joinPoint.getArgs(), annotation));
            operationLog.get().setTYPE(annotation.type());
        }




        StringBuffer requestLog = new StringBuffer();


        requestLog.append("请求信息：")
                .append("URL = {" + request.getRequestURI() + "},\t")
                .append("HTTP_METHOD = {" + request.getMethod() + "},\t")
                .append("IP = {" + request.getRemoteAddr() + "},\t")
                .append("DESCRIB={"+operationLog.get().getDESCRIB()+"},\t")
                .append("CLASS_METHOD = {" +operationLog.get().getCLASS()+"."+operationLog.get().getFUNCTION()+ "},\t");

        if(joinPoint.getArgs().length == 0) {
            requestLog.append("InputParam = {} ");
        } else {
            requestLog.append("InputParam = " +operationLog.get().getINPUTPARAM()+ "");
        }

        logger.info(requestLog.toString());
    }

    /**
     * 对当前登录用户和占位符处理
     * @param argNames 方法参数名称数组
     * @param args 方法参数数组
     * @param annotation 注解信息
     * @return 返回处理后的描述
     */
    private String getDetail(String[] argNames, Object[] args, OperationLogDetail annotation){

        Map<Object, Object> map = new HashMap<>(4);
        for(int i = 0;i < argNames.length;i++){
            map.put(argNames[i],args[i]);
        }

        String detail = annotation.detail();
        try {
            detail = "" + annotation.detail();
//            for (Map.Entry<Object, Object> entry : map.entrySet()) {
//                Object k = entry.getKey();
//                Object v = entry.getValue();
//                detail = detail.replace("{{" + k + "}}", JSON.toJSONString(v));
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return detail;
    }

    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        logger.info(Arrays.toString(joinPoint.getArgs()));
        System.out.println("进入方法前执行.....");

    }

    /**
     * 处理完请求，返回内容
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret) {
        try {
            //日志信息到kafka
//            kafkaMessager.sendCtrlMessage(operationLog.get().getFUNCTION(), operationLog.get().getMODULE(), operationLog.get().getUSERID(), operationLog.get().getLOG_LEVEL(),
//                    operationLog.get().getINPUTPARAM(), operationLog.get().getOUTPUTPARAM(), operationLog.get().getERRORMSG(), operationLog.get().getRUNTIME(), operationLog.get().getOTHERPARAM(),operationLog.get().getDESCRIB(),operationLog.get().getTYPE());

            logger.info("请求结果：" + operationLog.get().getOUTPUTPARAM());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            operationLog.remove();
        }
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing(pointcut="operationLog()",throwing = "e")
    public void doAfterThrowing(JoinPoint jp,Throwable e) throws Throwable{

        operationLog.get().setLOG_LEVEL("ERROR");
        operationLog.get().setERRORMSG(e.getMessage());

        e.printStackTrace();

        try {
            //日志信息到kafka
//            kafkaMessager.sendCtrlMessage(operationLog.get().getFUNCTION(), operationLog.get().getMODULE(), operationLog.get().getUSERID(), operationLog.get().getLOG_LEVEL(),
//                    operationLog.get().getINPUTPARAM(), operationLog.get().getOUTPUTPARAM(), operationLog.get().getERRORMSG(), operationLog.get().getRUNTIME(), operationLog.get().getOTHERPARAM(),operationLog.get().getDESCRIB(),operationLog.get().getTYPE());

            logger.error("出错信息"+e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        finally {
            operationLog.remove();

        }

       logger.info("方法异常时执行.....");
    }


    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    @After("operationLog()")
    public void doAfter(JoinPoint jp) {
        logger.info("4");

    }



}
