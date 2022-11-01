package com.example.product_service.controller;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.product_service.dto.UserDTO;
import com.example.product_service.repository.UserRespository;
import com.example.product_service.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class UserResource {

    private final UserService userService;
    private Logger log = LoggerFactory.getLogger(UserResource.class);
    private final UserRespository userRespository;
    private static final String ENTITY = "user-category";

    /**
     * 
     * @param registerRequest
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO registerRequest) throws URISyntaxException { 
        log.debug("Request to create a User: {} ", registerRequest);
        
        if (registerRequest.getId() == null) 
            throw new URISyntaxException("", "");
        else if (userRespository.findByUserName(registerRequest.getUserLogin().toLowerCase()).isPresent()) 
            throw new URISyntaxException("", "");
        else { 
            UserDTO user = userService.createUser(registerRequest);
            return ResponseEntity
                .created(new URI("/api/admin/users/" + registerRequest.getUserLogin()))
                .header(ENTITY, registerRequest.getUserLogin(), registerRequest.getId().toString())
                .body(user);
        }
    }
}
