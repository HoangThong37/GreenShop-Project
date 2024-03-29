package com.shopme.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Repository
public interface UserRepository extends SearchRepository<User, Integer> {
	@Query("SELECT u From User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id);
	
	// filter - tìm kiếm
	// @Query("SELECT u From User u where u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1%")
	@Query("SELECT u From User u WHERE CONCAT(u.id, ' ',u.email, ' ',u.firstName, ' ',u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE User u set u.enabled = ?2 WHERE u.id = ?1")
	@Modifying // cập nhật dữ liệu db
	public void updateEnabledAndStatus(Integer id, boolean enabled);

}
