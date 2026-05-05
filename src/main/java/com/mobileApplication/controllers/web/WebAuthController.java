package com.mobileApplication.controllers.web;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.mobileApplication.models.UserModel;
import com.mobileApplication.repositories.web.AdminRegUserRepository;
import com.mobileApplication.services.web.WebOtpService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebAuthController {
	
	private final AdminRegUserRepository registerRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebOtpService webOtpService;

    public WebAuthController(
            AdminRegUserRepository registerRepository,
            PasswordEncoder passwordEncoder,
            WebOtpService webOtpService
    ) {
        this.registerRepository = registerRepository;
        this.passwordEncoder = passwordEncoder;
        this.webOtpService = webOtpService;
    }

    @GetMapping("/")
        public String home() {
        return "ClinicRole"; // MUST match ClinicRole.html (no .html)
    }

    @GetMapping("/web/forgot")
    public String forgotPage() {
        return "ClinicFP";
    }

    @PostMapping("/web/forgot")
    public String sendForgotOtp(@RequestParam String email, HttpSession session) {
        String loginType = (String) session.getAttribute("loginType");

        Optional<UserModel> userOpt;

        if (loginType != null) {
            userOpt = registerRepository.findByEmailAndRole(email, loginType);
        } else {
            userOpt = registerRepository.findByEmail(email);
        }

        if (userOpt.isEmpty()) {
            session.setAttribute("modalType", "error");
            session.setAttribute("modalMessage", "Email not found.");
            return "redirect:/web/forgot";
        }

        UserModel user = userOpt.get();

        session.setAttribute("resetEmail", email);
        session.setAttribute("otpPurpose", "FORGOT_PASSWORD");

        webOtpService.sendForgotPasswordOtp(user);

        return "redirect:/web/forgot/verify";
    }

    @GetMapping("/web/forgot/verify")
    public String forgotVerifyPage() {
        return "ClinicOTP";
    }

    @PostMapping("/web/forgot/verify")
    public String verifyForgotOtp(@RequestParam String otp, HttpSession session) {
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            return "redirect:/web/forgot";
        }

        UserModel user = registerRepository.findByEmail(email).orElseThrow();

        boolean validOtp = webOtpService.verifyForgotPasswordOtp(user, otp);

        if (!validOtp) {
            session.setAttribute("otpModalType", "error");
            session.setAttribute("otpModalMessage", "Invalid or expired OTP.");
            return "redirect:/web/forgot/verify";
        }

        session.setAttribute("otpVerified", true);

        return "redirect:/web/forgot/reset";
    }

    @GetMapping("/web/forgot/reset")
    public String resetPasswordPage(HttpSession session) {

        Boolean verified = (Boolean) session.getAttribute("otpVerified");

        if (verified == null || !verified) {
            return "redirect:/web/forgot";
        }

        return "ClinicResetPassword"; 
    }

    @PostMapping("/web/forgot/reset")
    public String resetPassword(
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpSession session
    ) {
        String email = (String) session.getAttribute("resetEmail");
        Boolean verified = (Boolean) session.getAttribute("otpVerified");

        if (email == null || verified == null || !verified) {
            return "redirect:/web/forgot";
        }

        if (!password.equals(confirmPassword)) {
            session.setAttribute("modalType", "error");
            session.setAttribute("modalMessage", "Passwords do not match.");
            return "redirect:/web/forgot/reset";
        }

        UserModel user = registerRepository.findByEmail(email).orElseThrow();

        user.setHashedPassword(passwordEncoder.encode(password));

        user.setPasswordChangeCount(
            user.getPasswordChangeCount() == null ? 1 : user.getPasswordChangeCount() + 1
        );

        registerRepository.save(user);

        session.invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/role?reset=success";
    }

    @GetMapping("/adminRegister")
    public String adminRegisterPage(HttpSession session) {
        session.setAttribute("loginType", "ADMIN");
        return "AdminRegister";
    }

    @PostMapping("/registerProcess")
    public String registerAdmin(
            @RequestParam String fullname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String repassword,
            HttpSession session
    ) {
        if (!password.equals(repassword)) {
            session.setAttribute("modalType", "password");
            session.setAttribute("modalMessage", "Passwords do not match.");
            return "redirect:/adminRegister";
        }

        if (registerRepository.existsByEmail(email)) {
            session.setAttribute("modalType", "error");
            session.setAttribute("modalMessage", "Email already exists.");
            return "redirect:/adminRegister";
        }

        session.setAttribute("pendingAdminFullname", fullname);
        session.setAttribute("pendingAdminEmail", email);
        session.setAttribute("pendingAdminPassword", passwordEncoder.encode(password));
        session.setAttribute("otpPurpose", "REGISTER");
        session.setAttribute("loginType", "ADMIN");

        webOtpService.sendRegistrationOtp(email, "ADMIN");

        return "redirect:/web/register/verify";
    }

    @GetMapping("/web/register/verify")
    public String registerVerifyPage() {
        return "ClinicOTP";
    }

    @PostMapping("/web/register/verify")
    public String verifyRegisterOtp(@RequestParam String otp, HttpSession session) {
        String email = (String) session.getAttribute("pendingAdminEmail");
        String hashedPassword = (String) session.getAttribute("pendingAdminPassword");

        if (email == null || hashedPassword == null) {
            return "redirect:/adminRegister";
        }

        boolean validOtp = webOtpService.verifyRegistrationOtp(email, otp);

        if (!validOtp) {
            session.setAttribute("otpModalType", "error");
            session.setAttribute("otpModalMessage", "Invalid or expired OTP.");
            return "redirect:/web/register/verify";
        }

        if (registerRepository.existsByEmail(email)) {
            session.setAttribute("modalType", "error");
            session.setAttribute("modalMessage", "Email already exists.");
            return "redirect:/adminRegister";
        }

        UserModel user = new UserModel();
        user.setUsername(email);
        user.setEmail(email);
        user.setHashedPassword(hashedPassword);
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");

        registerRepository.save(user);

        session.removeAttribute("pendingAdminFullname");
        session.removeAttribute("pendingAdminEmail");
        session.removeAttribute("pendingAdminPassword");
        session.removeAttribute("otpPurpose");

        session.setAttribute("modalType", "success");
        session.setAttribute("modalMessage", "Admin account verified successfully. You can now login.");

        return "redirect:/adminLogin";
    }
    
    @PostMapping("/web/otp/resend")
    public String resendOtp(HttpSession session) {

        String purpose = (String) session.getAttribute("otpPurpose");

        if (purpose == null) return "redirect:/role";

        String email;

        if ("REGISTER".equals(purpose)) {
            email = (String) session.getAttribute("pendingAdminEmail");
        } else {
            email = (String) session.getAttribute("resetEmail");
        }

        if (email == null) return "redirect:/role";

        boolean success = webOtpService.resendOtp(email, purpose);

        if (!success) {
            session.setAttribute("otpModalType", "limit");

            if ("REGISTER".equals(purpose)) {
                return "redirect:/web/register/verify";
            }

            return "redirect:/web/forgot/verify";
        }

        session.setAttribute("otpModalType", "resent");
        session.setAttribute("otpModalMessage", "A new OTP has been sent.");

        if ("REGISTER".equals(purpose)) {
            return "redirect:/web/register/verify";
        }

        return "redirect:/web/forgot/verify";
    }

    @GetMapping("/adminLogin")
    public String adminLogin(HttpSession session) {
        session.setAttribute("loginType", "ADMIN");
        return "LoginAdmin";
    }

    @GetMapping("/doctorLogin")
    public String doctorLogin(HttpSession session) {
        session.setAttribute("loginType", "DENTIST");
        return "LoginDoctor";
    }

    @GetMapping("/receptLogin")
    public String receptLogin(HttpSession session) {
        session.setAttribute("loginType", "RECEPTIONIST");
        return "LoginReceptionist";
    }

    @GetMapping("/role")
    public String rolePage() {
        return "ClinicRole";
    }

    @PostMapping("/web/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {
        String loginType = (String) session.getAttribute("loginType");

        if (loginType == null) {
            return "redirect:/role";
        }

        Optional<UserModel> userOpt = registerRepository.findByEmailAndRole(email, loginType);

        if (userOpt.isEmpty()) {
            session.setAttribute("modalType", "error");
            session.setAttribute("modalMessage", "Invalid email or password.");
            return redirectByRole(loginType);
        }

        UserModel user = userOpt.get();

        if (user.getStatus() == null || !user.getStatus().equalsIgnoreCase("ACTIVE")) {
            session.setAttribute("modalType", "error");
            session.setAttribute("modalMessage", "Your account is inactive.");
            return redirectByRole(loginType);
        }

        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            session.setAttribute("modalType", "error");
            session.setAttribute("modalMessage", "Invalid email or password.");
            return redirectByRole(loginType);
        }

        session.setAttribute("WEB_USER_ID", user.getId());
        session.setAttribute("WEB_USER_EMAIL", user.getEmail());
        session.setAttribute("WEB_USER_ROLE", user.getRole());
        session.setMaxInactiveInterval(5 * 60);

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/web/admin/dashboard";
        }

        if ("DENTIST".equals(user.getRole())) {
            return "redirect:/web/doctor/dashboard";
        }

        if ("RECEPTIONIST".equals(user.getRole())) {
            return "redirect:/web/recepDashboard";
        }

        return "redirect:/role";
    }

    @GetMapping("/web/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/role";
    }

    private String redirectByRole(String role) {
        if ("ADMIN".equals(role)) {
            return "redirect:/adminLogin";
        }

        if ("DENTIST".equals(role)) {
            return "redirect:/doctorLogin";
        }

        if ("RECEPTIONIST".equals(role)) {
            return "redirect:/receptLogin";
        }

        return "redirect:/role";
    }

}
