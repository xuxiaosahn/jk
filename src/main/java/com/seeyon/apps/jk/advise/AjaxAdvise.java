package com.seeyon.apps.jk.advise;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>TODO</p>
 *
 * @author DELk
 * @@version 1.0.0
 * @since 2022/2/1
 */
public class AjaxAdvise {
    private static Log LOG = LogFactory.getLog(AjaxAdvise.class);

    public Object around(ProceedingJoinPoint pjp){
        Map<String,Object> res = new HashMap<>();
        try{
            res = (Map<String, Object>) pjp.proceed();
            res.put("success",true);
        }catch (Throwable th) {
            res.put("success",false);
            res.put("msg",th.getMessage());
        }
        return res;
    }

}
