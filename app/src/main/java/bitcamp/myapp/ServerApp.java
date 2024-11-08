package bitcamp.myapp;

import bitcamp.myapp.annotation.LoginUserArgumentResolver;
import bitcamp.myapp.interceptor.AdminInterceptor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@PropertySource("file:${user.home}/config/ncp.properties")
@EnableTransactionManagement // 스프링 프렘워크야, @Transactional 메서드가 붙은 클래스를 만나면 Proxy 클래스를 자동 생성하라!
public class ServerApp implements WebMvcConfigurer {

    @Autowired
    ApplicationContext appCtx;

    public ServerApp() {
        System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
    }

    public static void main(String[] args) {
        a = 100;
        SpringApplication.run(ServerApp.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/users*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}
