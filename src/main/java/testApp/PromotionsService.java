package testApp;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Value;

public class PromotionsService implements BeanNameAware, Service {
    @Value(property = "${promotions.service.id}")
    private String id;

    private String beanName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void introduce() {
        System.out.println("this is a PromotionService object");
    }
}
