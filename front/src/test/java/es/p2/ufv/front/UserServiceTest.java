package es.p2.ufv.front;

import es.p2.ufv.front.Register.User;
import es.p2.ufv.front.Register.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = Mockito.spy(new UserService());
    }

    @Test
    public void testSaveUserReturnsTrueOnSuccess() {
        User user = new User();
        user.setUsername("juan");
        user.setPassword("1234");

        doReturn(true).when(userService).saveUser(user);

        boolean result = userService.saveUser(user);
        assertTrue(result);
    }

    @Test
    public void testSaveUserReturnsFalseOnFailure() {
        User user = new User();
        user.setUsername("maria");
        user.setPassword("bad");

        doReturn(false).when(userService).saveUser(user);

        boolean result = userService.saveUser(user);
        assertFalse(result);
    }

    @Test
    public void testValidateUserReturnsTrueOnCorrectLogin() {
        doReturn(true).when(userService).validateUser("juan", "1234");

        boolean result = userService.validateUser("juan", "1234");
        assertTrue(result);
    }

    @Test
    public void testValidateUserReturnsFalseOnWrongLogin() {
        doReturn(false).when(userService).validateUser("juan", "wrong");

        boolean result = userService.validateUser("juan", "wrong");
        assertFalse(result);
    }
}
