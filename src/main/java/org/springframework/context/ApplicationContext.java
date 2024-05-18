package org.springframework.context;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FileScanner;
import org.springframework.exceptions.*;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class ApplicationContext {

    private final BeanFactory beanFactory = new BeanFactory();

    public ApplicationContext(String basePackage)
            throws ReflectiveOperationException, URISyntaxException, BeanException, ConfigurationsException, ScheduledMethodException, PropertyException, IOException, SpringTestException {
        if (!testApplicationRun(basePackage)) {
            beanFactory.instantiate(basePackage);
        }

        beanFactory.populateBeans();
        beanFactory.populateProperties();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();
        beanFactory.startScheduleThread();
    }

    public ApplicationContext(Class<?> configuration)
            throws ReflectiveOperationException, URISyntaxException, BeanException, ScheduledMethodException, IOException, PropertyException {

        beanFactory.instantiate(configuration);
        beanFactory.populateBeans();
        beanFactory.populateProperties();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();
        beanFactory.startScheduleThread();
    }

    private boolean testApplicationRun(String basePackage) throws URISyntaxException, ReflectiveOperationException, ConfigurationsException, ScheduledMethodException, BeanException, SpringTestException {
        Class<?> testClass = FileScanner.getSpringTestFile(basePackage);
        if (testClass == null) {
            return false;
        }
        beanFactory.testInstantiate(testClass, basePackage);
        return true;
    }


    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void close() {
        beanFactory.close();
    }

    public Map<String, Object> getBeanFactorySingletons () {
        return beanFactory.getSingletons();
    }
}
