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

    public List<Order> getUserOrders(Long id, int page, int limit) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        return orderRepository.getUserOrdersById(id,pageable);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<User> findAllBySimilarEmail(String email){
        return userRepository.findAllBySimilarEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
