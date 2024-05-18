package testApp;

import org.springframework.beans.factory.annotation.MockBean;
import org.springframework.beans.factory.annotation.ProductServiceMark;
import org.springframework.context.ApplicationContext;
import org.springframework.exceptions.BeanException;
import org.springframework.exceptions.PropertyException;
import org.springframework.exceptions.ScheduledMethodException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.util.*;

public class UserTest {
    // Important remarks:
    //
    // UserTest class is a class where user tests his application written on our Spring.
    // Here listed the fields of UserTest class.
    //
    // User must list here all the fields he wants to use while testing.
    //
    // Each of beans should implement Service interface and be marked by special annotation
    // like @<className>Mark
    //
    // Example: @ProductServiceMark
    //          private Service productService;
    //
    // If user wants to use MockBean, he should use annotation @MockBaan

    @MockBean
    @ProductServiceMark
    private Service productService;

    public void run() throws ReflectiveOperationException, ScheduledMethodException, PropertyException, BeanException, URISyntaxException, IOException, InterruptedException {
        ApplicationContext applicationContext = new ApplicationContext(
                MyApplicationContextConfiguration.class);

        List<Field> allFields = Arrays.asList(UserTest.class.getDeclaredFields());

        for (Field field: allFields) {
            Annotation[] annotations = field.getDeclaredAnnotations();

            if (hasMockBeanAnnotation(annotations)) {
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);

                    Map<String, Object> singletons = applicationContext.getBeanFactorySingletons();
                    for (String beanName: singletons.keySet()) {
                        String beanNameWithoutPrefix = beanName.substring(beanName.lastIndexOf('.') + 1);

                        // the line below is to check whether field has an annotation named
                        // 'after' this bean or not. For example, that is equal to using of "ProductService" field
                        // without marking with annotation instead of using "Service" field, marked as
                        // @ProductServiceMark to show that this field is a ProductService object.
                        if (hasSpecificAnnotation(annotations, beanNameWithoutPrefix + "Mark()")) {
                            // if so, we will replace this bean by a proxy.

                            Service realBean = (Service) singletons.get(beanName);
                            ClassLoader vasiaClassLoader = realBean.getClass().getClassLoader();

                            Class[] interfaces = realBean.getClass().getInterfaces();

                            Service proxyBean = (Service) Proxy.newProxyInstance(vasiaClassLoader, interfaces, new ServiceInvocationHandler(realBean));
                            field.set(this, proxyBean);
                        }
                    }

                    field.setAccessible(accessible);
                } catch (SecurityException
                         | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        Thread.sleep(5000);
        System.out.println("close");
        applicationContext.close();

        // here and below is user's code with testing
    }

    public static void main(String[] args) throws ReflectiveOperationException, ScheduledMethodException, PropertyException, BeanException, URISyntaxException, IOException, InterruptedException {
        UserTest test = new UserTest();
        test.run();
    }

    private boolean hasMockBeanAnnotation(Annotation[] annotations) {
        return hasSpecificAnnotation(annotations, "MockBean()");
    }

    private boolean hasSpecificAnnotation(Annotation[] annotations, String particularAnnotationName) {
        for (var annotation: annotations) {
            String annotationName = annotation.toString();
            annotationName = annotationName.substring(annotationName.lastIndexOf(".") + 1);

            if (particularAnnotationName.equals(annotationName)) {
                return true;
            }
        }

        return false;
    }
}
