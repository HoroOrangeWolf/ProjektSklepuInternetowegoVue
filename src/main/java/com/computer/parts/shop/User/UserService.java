package com.computer.parts.shop.User;

import com.computer.parts.shop.Order.Order;
import com.computer.parts.shop.Order.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    public User getUserById(Long userId){
        Optional<User> byId = userRepository.findById(userId);

        if(byId.isEmpty())
            throw new IllegalStateException("User not found");

        return byId.get();
    }

    public void registerUser(User user){
        userRepository.save(user);
    }

    public void deleteById(Long idUser){
        userRepository.deleteById(idUser);
    }

    public List<User> findAllBySimilarEmail(String email, Integer page, Integer limit, String sortBy, Sort.Direction direction){

        Sort.Order order = Sort.Order
                .by(sortBy)
                .with(direction);

        Sort sort = Sort.by(
                List.of(
                        order
                )
        );

        Pageable pageable = PageRequest.of(
                page,
                limit,
                sort
        );

        return userRepository.findAllBySimilarEmail(email, pageable);
    }

    public Long countAllBySimilarEmail(String email){
        return userRepository.countAllBySimilarEmail(email);
    }

    public void blockUser(Long userId, Boolean block){
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty())
            throw new IllegalStateException("User not found");

        User user = optionalUser.get();

        user.setIsAccountLocked(block);

    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
