package org.springframework.context;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FileScanner;
import org.springframework.exceptions.*;


import java.io.IOException;
import java.net.URISyntaxException;

public class ApplicationContext {

    private final BeanFactory beanFactory = new BeanFactory();

    public ApplicationContext(String basePackage)
            throws ReflectiveOperationException, URISyntaxException, BeanException, ConfigurationsException, ScheduledMethodException, IncorrectClassPropertyException, PropertyNotFoundException, PropertiesSourceException, IOException, PropertyFormatException, SpringTestFileException, SpringTestConfigurationException {
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
            throws ReflectiveOperationException, URISyntaxException, BeanException, ScheduledMethodException, IncorrectClassPropertyException, PropertyNotFoundException, PropertiesSourceException, IOException, PropertyFormatException {
        beanFactory.instantiate(configuration);
        beanFactory.populateBeans();
        beanFactory.populateProperties();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();
        beanFactory.startScheduleThread();
    }

    private boolean testApplicationRun(String basePackage) throws SpringTestFileException, URISyntaxException, ReflectiveOperationException, ConfigurationsException, ScheduledMethodException, BeanException, SpringTestConfigurationException {
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
}
