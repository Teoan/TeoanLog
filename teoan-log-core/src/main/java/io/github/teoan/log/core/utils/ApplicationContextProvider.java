package io.github.teoan.log.core.utils;


import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author Teoan
 * @since 2023/10/18 22:59
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }


    public static <T> T getBean(Class<T> tClass) throws BeansException{
        if(ObjectUtils.isEmpty(applicationContext)){
            return null;
        }
        return applicationContext.getBean(tClass);
    }


}
