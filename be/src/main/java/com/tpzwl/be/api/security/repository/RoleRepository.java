package com.tpzwl.be.api.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tpzwl.be.api.model.RoleCount;
import com.tpzwl.be.api.security.model.EnumRole;
import com.tpzwl.be.api.security.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(EnumRole name);
  
	@Query(nativeQuery = true)
	List<RoleCount> countByRole();
}
