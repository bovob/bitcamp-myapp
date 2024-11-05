package bitcamp.myapp.annotation;

import bitcamp.myapp.vo.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        System.out.println("LoginUserArgumentResolver.supportsParameter() 호출됨!");

        // 파라미터에 대해 이 해결사를 동원할 수 있는지 여부를 알려준다.
        // 다음 두 조건을 만족해야만 파라미터 값을 준다.
        // @LoginUser 애노테이션이 붙어있어야 하며
        // User 객체를 받을 수 있는 타입어이여 한다.
        return parameter.hasParameterAnnotation(LoginUser.class) && parameter.getParameterType()
            .isAssignableFrom(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        System.out.println("LoginUserArgumentResolver.resolveArgument() 호출됨!");
        // 파라미터 값을 리턴한다.
        // 즉, 로그인사용자 정보(USER)를 세션에서 꺼내 리턴한다.
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();
        return session.getAttribute(parameter.getParameterName());
    }
}
