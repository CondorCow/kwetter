/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package services;

import domain.Kweet;
import domain.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
@javax.ejb.Startup
public class Startup {
    @Inject
    private KweetService kwetterService;
    @Inject
    private UserService userService;


    @PostConstruct
    public void initData() {
        User user = userService.newUser("dannyajc", "jea6");
        User admin = userService.newUser("admin", "admin");


        Kweet userKweet = new Kweet("Kan ik al hashtags gebruiken in deze app? #wordtweltijd", user);
        kwetterService.newKweet(userKweet);

        Kweet adminKweet = new Kweet("@modder2 doe normaal", admin);
        kwetterService.newKweet(adminKweet);


        userService.newUser("modder", "jea6");
        User modder2 = userService.newUser("modder2", "jea6");
        userService.followUser(1, 2);
        userService.followUser(1, 3);

        Kweet modderKweet = new Kweet("Ik ben niet zomaar een moderator, ik ben moderator #2", modder2);
        kwetterService.newKweet(modderKweet);
    }
}
