package com.bluebell.radicle.repositories.security;

import com.bluebell.platform.models.core.entities.security.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link User} entities
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, CrudRepository<User, Long> {

    /**
     * Attempts to look up a {@link User} by their username
     *
     * @param username username
     * @return {@link User}, can return null
     */
    User findUserByUsername(final String username);

    /**
     * Attempts to look up a {@link User} by their email
     *
     * @param email email
     * @return {@link User}, can return null
     */
    User findUserByEmail(final String email);

    /**
     * Deletes a {@link User} by their email
     *
     * @param email email
     */
    void deleteUserByEmail(final String email);
}
