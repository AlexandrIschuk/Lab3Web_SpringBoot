package ru.ssau.todo.Service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.entity.UserDto;
import ru.ssau.todo.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    MappingUtils mappingUtils = new MappingUtils();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDto> findByUsername(String username){
        return Optional.ofNullable(mappingUtils.mapToUserDto(userRepository.findByUsername(username).orElseThrow()));
    }
}
