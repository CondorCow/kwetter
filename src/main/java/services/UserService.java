package services;

import com.mysql.cj.core.util.StringUtils;
import dao.IUserDao;
import dao.JPA;
import domain.User;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
//@Stateless
public class UserService {
    @Inject
    @JPA
    IUserDao userDao;

    public UserService() {
        super();
    }

    /**
     * @param username : the users username
     * @param password : the users password to log in
     * @return User, created
     */
    public User newUser(String username, String password) {
        return (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password))
                ? null
                : userDao.create(new User(username, password, User.Role.USER));
    }

    /**
     * @param username : the username of the user being found
     * @return User : the User-object that was found in the userDao
     */
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * Edits the user's fields, according to the changes within the 'new' user
     * @param toUpdateUser : the user to be updated
     * @return User : the updated user
     */
    public User updateUser(User toUpdateUser) {
        User user = userDao.findByUsername(toUpdateUser.getUsername());
        if (user == null) return null;

        String bio = toUpdateUser.getBio();
        String location = toUpdateUser.getLocation();
        String site = toUpdateUser.getWebsite();
        String email = toUpdateUser.getEmail();
        String profilePic = toUpdateUser.getProfilePicture();

        if (bio != null)
            user.setBio(bio);
        if (location != null)
            user.setLocation(location);
        if (site != null)
            user.setWebsite(site);
        if (email != null)
            user.setEmail(email);
        if (profilePic != null)
            user.setProfilePicture(profilePic);

        return userDao.update(user);
    }

    /**
     * @param id : identifier of the user to be deleted
     */
    public void deleteUser(long id) {
        User user = userDao.findById(id);
        if (user != null) {
//            return;
            userDao.delete(user);
        }
    }

    /**
     * Searches for the user and checks if the filled in password is correct
     * @param user : user who will be authenticated
     * @return boolean : user authorized or not
     */
    public boolean authorizeUser(User user) {
        User originalUser = userDao.findByUsername(user.getUsername());
        return originalUser != null && originalUser.getPassword().equals(user.getPassword());
    }

    /**
     * @return List<User> : list of all the users in Kwetter
     */
    //Getter for all users
    public List<User> getUsers() {
        return userDao.findAll();
    }

    /**
     * @param id : identifier of the main user
     * @param unfollowingId : the identifier of the user to be unfollowed
     * @return boolean : returns whether or not the unfollow method was passed
     */
    public boolean unfollowUser(long id, long unfollowingId) {
        if (id != unfollowingId){
            User mainUser = userDao.findById(id);
            User toUnfollowUser = userDao.findById(unfollowingId);

            if (mainUser == null || toUnfollowUser == null || !toUnfollowUser.removeFollower(mainUser) || !mainUser.removeFollowing(toUnfollowUser) ) {
                return false;
            }
            userDao.update(toUnfollowUser);
            userDao.update(mainUser);
            return true;
        }
        return false;
    }

    /**
     * Follows a user for the main (logged in) user
     * and adds this user to the followers list of the other user
     * @param id : identifier of the user that will follow
     * @param toFollowId:  identifier of the to be followed user
     * @return boolean : returns whether the user was successfully followed or not
     */
    public boolean followUser(long id, long toFollowId) {
        if (id != toFollowId) {
            User mainUser = userDao.findById(id);
            User toFollowUser = userDao.findById(toFollowId);

            if (mainUser == null || toFollowUser == null || !toFollowUser.addFollower(mainUser) || !mainUser.addFollowing(toFollowUser) ) {
                return false;
            }
            userDao.update(toFollowUser);
            userDao.update(mainUser);
            return true;
        }
        return false;
    }

    /**
     * @param id : identifier of the user which's role has to be changed
     * @param newRole : the new role to be assigned
     * @return User : returns the new User with his new role
     */
    public User editRole(long id, User.Role newRole) {
//        if(id == null) return null;
        User user = userDao.findById(id);
        if (user == null || newRole == null) {
            return null;
        }

        user.setRole(newRole);
        return userDao.update(user);
    }

    // Getters for the following and followers list
    public List<User> getFollowing(long id) {
        return userDao.findFollowing(id);
    }

    public List<User> getFollowers(long id) {
        return userDao.findFollowers(id);
    }
}
