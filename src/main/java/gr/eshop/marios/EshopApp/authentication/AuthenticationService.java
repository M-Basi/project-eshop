package gr.eshop.marios.EshopApp.authentication;

import gr.eshop.marios.EshopApp.core.enums.Role;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotAuthorizedException;
import gr.eshop.marios.EshopApp.dto.AuthenticationRequestDTO;
import gr.eshop.marios.EshopApp.dto.AuthenticationResponseDTO;
import gr.eshop.marios.EshopApp.model.AdminUser;
import gr.eshop.marios.EshopApp.model.Customer;
import gr.eshop.marios.EshopApp.model.User;
import gr.eshop.marios.EshopApp.repository.AdminUserRepository;
import gr.eshop.marios.EshopApp.repository.CustomerRepository;
import gr.eshop.marios.EshopApp.repository.UserRepository;
import gr.eshop.marios.EshopApp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AdminUserRepository adminUserRepository;


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        String firstname= "";
        String uuid = user.getUuid();

        if (user.getRole() == Role.CUSTOMER_USER) {
            Optional<Customer> customer = customerRepository.findByUserUuid(user.getUuid());
            if (customer.isEmpty()) {
                firstname = "User";

            } else {
                firstname = customer.get().getFirstname();
            }

        } else if (user.getRole() == Role.ADMIN_USER) {
            Optional<AdminUser> admin = adminUserRepository.findByUserUuid(user.getUuid());
            if (admin.isEmpty()) {
                firstname = "Admin";
            } else {
                firstname = admin.get().getFirstname();

            }
        } else {
            firstname = "SUPER_ADMIN";

        }

        return new AuthenticationResponseDTO(firstname, uuid, token);
    }
}




