package com.wehgu.weixin.utils;

import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JFinalControllerFactory extends com.jfinal.core.ControllerFactory{
    private final ThreadLocal<Map<Class<? extends Controller>, Controller>> buffers = ThreadLocal.withInitial(HashMap::new);

    @Override
    public Controller getController(Class<? extends Controller> controllerClass) throws ReflectiveOperationException {
        Controller controller = buffers.get().get(controllerClass);
        if(controller==null){
            controller=controllerClass.newInstance();
            autoInject(controller);
            buffers.get().put(controllerClass,controller);
        }
        return controller;
    }

    public void autoInject(Controller controller){
        Field[] fields = controller.getClass().getDeclaredFields();
        for (Field field:fields){
            Object bean=null;
            if(field.isAnnotationPresent(Autowired.class)){
                try {
                    bean = SpringBootApplicationContextUtils.getBean(field.getName());
                }catch (Exception ex){
                    log.error(ex.getMessage(),ex);
                }
                if(null==bean){
                    try {
                        bean= SpringBootApplicationContextUtils.getBean(field.getType());
                    }catch (Exception ex){
                        log.error(ex.getMessage(),ex);
                    }
                }
                if(null==bean){
                    Autowired annotation = field.getAnnotation(Autowired.class);
                    if(annotation.required()){
                        throw new RuntimeException(String.format("Error creating bean with name '%s': Injection of autowired dependencies failed.", field.getName()));
                    }
                }
            }else if(field.isAnnotationPresent(Resource.class)){
                Resource annotation = field.getAnnotation(Resource.class);
                try {
                    bean= SpringBootApplicationContextUtils.getBean(annotation.name().trim().length() == 0 ? field.getName() : annotation.name());
                }catch (Exception ex){
                    log.error(ex.getMessage(),ex);
                }
                if(null==bean){
                    try {
                        bean= SpringBootApplicationContextUtils.getBean(field.getType());
                    }catch (Exception ex){
                        log.error(ex.getMessage(),ex);
                    }
                }
            }else {
                if (injectDependency) {
                    com.jfinal.aop.Aop.inject(controller);
                }
            }
            if(bean!=null){
                try {
                    field.setAccessible(true);
                    field.set(controller,bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}