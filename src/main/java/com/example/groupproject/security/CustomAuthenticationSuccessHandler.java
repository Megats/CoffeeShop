package com.example.groupproject.security;

//import com.example.groupproject.entity.User;
import com.example.groupproject.model.CustomUserDetails;
import com.example.groupproject.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
    @Autowired
    private UserRepository userRepository;

//    private static final Logger logger = Logger.getLogger(CustomAuthenticationSuccessHandler.class.getName());
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                    throws IOException, ServletException, RuntimeException {

        //session.setAttribute("username", user.getUsername());
        HttpSession session = request.getSession();
        CustomUserDetails authUser = (CustomUserDetails) authentication.getPrincipal();
        session.setAttribute("email", authUser.getEmail());// Set the email in the session
        String redirectUrl = determineTargetUrl(authentication);
        //set response to OK status
        response.setStatus(HttpServletResponse.SC_OK);
        //response.sendRedirect(redirectUrl);
        response.sendRedirect(request.getContextPath() + redirectUrl);

    }

    protected String determineTargetUrl(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("SELLER"));
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("CUSTOMER"));

        if (isAdmin) {
            return "/admin/index";
        } else if (isCustomer) {
            return "/";
        } else {
            throw new IllegalStateException();
        }
    }

}
