package bg.sofia.uni.fmi.mjt.wish.list;

import org.junit.Test;

public class WishListClientTest {

    @Test
    public void testWistListClientDummyTest() {
        try {
            WishListServer server = new WishListServer(7777);
            server.start();
            Thread.sleep(100);
            server.stop();
        } catch (InterruptedException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }
}
