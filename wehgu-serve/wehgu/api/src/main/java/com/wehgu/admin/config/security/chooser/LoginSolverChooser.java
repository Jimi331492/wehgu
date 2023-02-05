package com.wehgu.admin.config.security.chooser;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSolverChooser implements ApplicationContextAware {


    private final Map<String, LoginSolver> chooseMap = new HashMap<>();

    public LoginSolver choose(String type) {
        return chooseMap.get(type);
    }

    @PostConstruct
    public void register(){
        Map<String,LoginSolver> solverMap = context.getBeansOfType(LoginSolver.class);
        for (LoginSolver solver : solverMap.values()){
            for (String support : solver.supports()){
                chooseMap.put(support,solver);
            }
        }
    }

    private ApplicationContext context;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
