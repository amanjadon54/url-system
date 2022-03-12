package service;

import api.UrlShortner;
import model.User;
import org.junit.Before;
import org.junit.Test;
import repo.UrlRepository;
import repo.UserRepository;
import service.impl.UrlManagerServiceImpl;
import api.UrlShortnerImpl;
import service.impl.UserServiceImpl;
import strategy.RandomUniqueKeyGeneratorStrategy;
import strategy.UniqueKeyStrategy;

import java.util.LinkedList;

import static constant.shortnerConstant.INVALID_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UrlShortnerImplTest {

    private UrlShortner urlShortner;

    @Before
    public void init() {
        User userA = new User("U12345", "Jack", "PersonA", true, new LinkedList<>());
        User userB = new User("U23456", "Jill", "PersonB", true, new LinkedList<>());
        User userC = new User("U34567", "Jonny", "PersonC", true, new LinkedList<>());

        UniqueKeyStrategy uniqueKeyStrategy = new RandomUniqueKeyGeneratorStrategy();
        UserRepository userRepository = new UserRepository();
        UrlRepository urlRepository = new UrlRepository();

        UserService userService = new UserServiceImpl(userRepository);
        userService.add(userA);
        userService.add(userB);
        userService.add(userC);
        UrlManagerService urlManagerService = new UrlManagerServiceImpl(urlRepository, uniqueKeyStrategy);
        urlShortner = new UrlShortnerImpl(urlManagerService, userService);

    }

    @Test
    public void testRegisteredUsersShortener() {
        String firstGoogle = urlShortner.put("google.com", "U12345", 0);
        String secondYoutube1 = urlShortner.put("youtube.com", "U23456", 0);
        String secondYouTube2 = urlShortner.put("youtube.com", "U23456", 0);
        String secondGoogle = urlShortner.put("google.com", "U23456", 0);

        String orignialSecondYoutubeUrl = urlShortner.get(secondYouTube2);
        String originalFirstGoolgeUrl = urlShortner.get(firstGoogle);
        assertEquals(orignialSecondYoutubeUrl, "youtube.com");
        assertEquals(originalFirstGoolgeUrl, "google.com");
    }

    @Test
    public void testDeactivateUser() {

        String existingUrl = urlShortner.put("abc.com", "U12345", 0);
        urlShortner.deactivate("U12345");
        Exception exception = assertThrows(RuntimeException.class, () -> {
            urlShortner.get(existingUrl);
        });
        assertEquals(exception.getMessage(), INVALID_URL);
    }

    @Test
    public void testReactivateUser() {
        String userId = "U34567";

        String existingUrl = urlShortner.put("abcd.com", userId, 0);
        String existingUrl2 = urlShortner.put("efgh.com", userId, 0);
        urlShortner.deactivate(userId);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            urlShortner.get(existingUrl);
        });
        assertEquals(exception.getMessage(), INVALID_URL);

        urlShortner.reactivate(userId);
        assertEquals(urlShortner.get(existingUrl2), "efgh.com");
    }

    @Test
    public void testExpiry() throws InterruptedException {
        String userId = "U34567";
        String existingUrl = urlShortner.put("abcd.com", userId, 1000);
        assertEquals(urlShortner.get(existingUrl), "abcd.com");

        Thread.sleep(1000);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            urlShortner.get(existingUrl);
        });
        assertEquals(exception.getMessage(), INVALID_URL);
    }

}
