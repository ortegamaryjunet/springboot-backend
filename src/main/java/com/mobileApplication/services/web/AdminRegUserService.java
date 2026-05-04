//package com.mobileApplication.services.web;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.mobileApplication.repositories.web.AdminRegUserRepository;
//
//@Service
//public class AdminRegUserService {
//	
//	private final AdminRegUserRepository userRepository;
//    private final LogUserRepository logUserRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public RegUserService(AdminRegUserRepository userRepository,
//                          LogUserRepository logUserRepository,
//                          PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.logUserRepository = logUserRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public RegUser registerUser(RegUser user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }
//
//    @Override
//    public RegUser loginUser(String email, String password) {
//        Optional<RegUser> user = userRepository.findByEmail(email);
//
//        if (user.isPresent()
//                && user.get().isVerified()
//                && passwordEncoder.matches(password, user.get().getPassword())) {
//
//            LogUser logUser = new LogUser();
//            logUser.setEmail(user.get().getEmail());
//            logUser.setPassword(user.get().getPassword());
//
//            logUserRepository.save(logUser);
//
//            return user.get();
//        }
//
//        return null;
//    }
//
//    @Override
//    public boolean emailExists(String email) {
//        return userRepository.findByEmail(email).isPresent();
//    }
//
//}
