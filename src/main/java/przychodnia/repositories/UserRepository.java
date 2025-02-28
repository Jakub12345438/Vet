package przychodnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import przychodnia.models.UserModel;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query("SELECT u FROM UserModel u WHERE u.email = :email")
    UserModel findByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserModel u WHERE u.userID = :userId")
    UserModel findUserById(@Param("userId") Long userId);

    @Query("SELECT u FROM UserModel u JOIN u.roles r WHERE r.roleName = :roleName")
    List<UserModel> findByRoleName(@Param("roleName") String roleName);

    @Modifying
    @Query("UPDATE UserModel u SET u.password = :password WHERE u.userID = :id")
    void updatePassword(@Param("password") String password, @Param("id") Long id);


}