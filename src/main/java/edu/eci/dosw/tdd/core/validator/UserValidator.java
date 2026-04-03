package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validate(User user) {
        ValidationUtil.validateNotNull(user, "El usuario no puede ser nulo");
        ValidationUtil.validateNotBlank(user.getId(), "El ID del usuario no puede estar vacío");
        ValidationUtil.validateNotBlank(user.getName(), "El nombre no puede estar vacío");
        ValidationUtil.validateNotNull(user.getRole(), "El rol del usuario no puede ser nulo");
        ValidationUtil.validateNotBlank(user.getUsername(), "El username no puede estar vacío");
        ValidationUtil.validateNotBlank(user.getPassword(), "La contraseña no puede estar vacía");
    }

}