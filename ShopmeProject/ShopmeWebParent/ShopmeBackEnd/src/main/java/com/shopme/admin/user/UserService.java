package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
	public static final int USER_PER_PAGE = 4;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// find all user
	public List<User> listAll() {
		return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
	}

	// get email
	public User getEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	public Page<User> listByPage(int number, String sortField, String sortDir, String keyword) {
		// sortDir : asc or desc
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(number - 1, USER_PER_PAGE, sort);

		if (keyword != null) {
			return userRepository.findAll(keyword, pageable);
		}
		return userRepository.findAll(pageable);
	}

	//
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	public User save(User user) {
		boolean isUpdateUser = (user.getId() != null); // xem id có null k
		if (isUpdateUser == true) { // có id user
			User exitUser = userRepository.findById(user.getId()).get(); // get id user
			if (user.getPassword().isEmpty()) { // nếu password null thì set vào
				user.setPassword(exitUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		return userRepository.save(user);
	}

	// update account detail
	public User updateUserAccount(User userInform) {
		User userInDB = userRepository.findById(userInform.getId()).get();

		if (!userInform.getPassword().isEmpty()) { // check password
            userInDB.setPassword(userInform.getPassword());
            encodePassword(userInDB);
		} 
		// check photo
		if (userInform.getPhotos() != null) {
			userInDB.setPhotos(userInform.getPhotos());
			encodePassword(userInDB);
		}
		userInDB.setFirstName(userInform.getFirstName());
		userInDB.setLastName(userInform.getLastName());
		//userInDB.setEnabled(userInform);
		
		return userRepository.save(userInDB);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);

		if (userByEmail == null) {
			return true;
		}
		boolean isCreatingNew = (id == null);
		if (isCreatingNew) {
			if (userByEmail != null) { // not email
				return false;
			}
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID : " + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException {
		Long idDelete = userRepository.countById(id);
		if (idDelete == null || idDelete == 0) {
			throw new UserNotFoundException("Could not find any user with ID : " + id);
		}
		userRepository.deleteById(id);

	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepository.updateEnabledAndStatus(id, enabled);
	}

}
