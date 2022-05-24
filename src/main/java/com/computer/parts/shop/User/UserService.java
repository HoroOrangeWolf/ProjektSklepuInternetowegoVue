package com.computer.parts.shop.User;

import com.computer.parts.shop.Exceptions.BadRequestException;
import com.computer.parts.shop.Order.Order;
import com.computer.parts.shop.Order.OrderRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private OrderRepository orderRepository;

  private UserRepository userRepository;

  public User getUserById(Long userId) {
    Optional<User> byId = userRepository.findById(userId);

    if (byId.isEmpty()) throw new BadRequestException("User not found");

    return byId.get();
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public void registerUser(User user) {
    userRepository.save(user);
  }

  public void deleteById(Long idUser) {
    userRepository.deleteById(idUser);
  }

  public List<User> findAllBySimilarEmail(
    String email,
    Integer page,
    Integer limit,
    String sortBy,
    Sort.Direction direction
  ) {
    Sort.Order order = Sort.Order.by(sortBy).with(direction);

    Sort sort = Sort.by(List.of(order));

    Pageable pageable = PageRequest.of(page, limit, sort);

    return userRepository.findAllBySimilarEmail(email, pageable);
  }

  public Long countAllBySimilarEmail(String email) {
    return userRepository.countAllBySimilarEmail(email);
  }

  public void blockUser(Long userId, Boolean block) {
    Optional<User> optionalUser = userRepository.findById(userId);

    if (optionalUser.isEmpty()) throw new BadRequestException("User not found");

    User user = optionalUser.get();

    user.setIsAccountLocked(block);

    userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {

    Optional<User> byEmail = userRepository.findByEmail(email);

    if(byEmail.isEmpty())
        throw new UsernameNotFoundException("User is not found");

    User user = byEmail.get();

    if(!user.getIsEnabled())
      throw new BadRequestException("User is disabled");

    return user;
  }

  public void updateUser(User user) {
    userRepository.save(user);
  }
}
