/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitter_random;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import net.sourceforge.jnlp.util.FileUtils;
import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.json.DataObjectFactory;

/**
 *
 * @author shaleen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException, InterruptedException, IOException, JSONException {

        System.out.println("succesful");


        // The factory instance is re-useable and thread safe.
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
               cb.setIncludeEntitiesEnabled(true);
        cb.setIncludeRTsEnabled(true);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        //insert the appropriate consumer key and consumer secret here
        
        twitter.setOAuthConsumer("UsUvdkCwTfaVsRTCdTI4uQ",
                "vXiwfEAB0NNX99TRn4WrLYZ6ApKZ8nM2DjxAh474k");

        RequestToken requestToken = twitter.getOAuthRequestToken();


//Username idVinayak Sood 293547099

        AccessToken accessToken = new AccessToken("304839785-IViLmo7jOAKRRW4cZlUUthcxmffNIj9dLcEjgLWs", "w0NwhdbWmZOcy0hrkGWUn7fmmFsKffkGYb55lxdWyg");

        twitter.setOAuthAccessToken(accessToken);

        IDs ids = null;

 ArrayList<Long> bfsarray = new ArrayList<Long>();
                File dir = new File("//home//shaleen//Twitter//twitter");
// please copy the user and tweet files from the main twitter code. This code only generates friends and followers
                // list of the users in the main directory.
        for(File child : dir.listFiles()) {
            if(child.isDirectory()) {
                String name = child.getName();
               bfsarray.add(Long.valueOf(name));
               System.out.println(Long.valueOf(name));
            }
        }

        long[] arr = new long[100];
        for( int i=0 ; i< 100; i++) {
            arr[i] = bfsarray.get(i);
        }
try{
    //lookUpUsers(twitter, arr);// eliminate if you can copy this from twitter folder by renaming them.
//        lookUpTweets(twitter,arr);// eliminate if you can copy this from twitter folder by renaming them.
        lookUpFriends(twitter, arr);
        lookUpFollowers(twitter, arr);
}catch(Exception e) {
    System.out.println(e.getLocalizedMessage());
}


    }

     private static void lookUpTweets(Twitter twitter, long[] arr) throws TwitterException, InterruptedException {


      for(int i = 0;i<arr.length;i++) {
           ResponseList<Status> status = null;
          try {
              status = twitter.getUserTimeline(arr[i]);
          }catch(TwitterException t){
             System.out.println("TwitterException caught:" + t.getErrorMessage() + t.getExceptionCode());
                RateLimitStatus rate = t.getRateLimitStatus();
                if (rate == null) {
                    rate = twitter.getRateLimitStatus();
                }
                System.out.println("hourly limit:" + rate.getHourlyLimit());
                System.out.println("remaining hits:" + rate.getRemainingHits());
                System.out.println("reset time:" + rate.getResetTimeInSeconds());
                System.out.println("time until reset:" + rate.getSecondsUntilReset());
                if (rate.getRemainingHits() == 0) {
                    System.out.println("Sleeping start");
                    Thread.sleep(rate.getSecondsUntilReset() * 1001);
                    System.out.println("Sleeping end");
                    i--;
                }
        }
           try {
             if (status != null && twitter.getRateLimitStatus().getRemainingHits() != 350) {
                                System.out.println(DataObjectFactory.getRawJSON(status));
                                JSONArray json = new JSONArray(DataObjectFactory.getRawJSON(status));
                                File dir = new File("//home//shaleen//Twitter//Rajan2");

                                File file = new File(dir, "tweets_"+arr[i]);
                                BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
                                output.write(json.toString(5));
                                output.flush();
                            }
        } catch (Exception e) {
            System.out.println("in for loop"+e.getMessage());
}
      }

    }


private static void lookUpFollowers(Twitter twitter, long[] arr) throws TwitterException, InterruptedException, IOException {


for(int i = 0;i<arr.length;i++) {
 IDs ids = null;
 ids=null;
  try{
        ids = twitter.getFollowersIDs(arr[i], -1);
        } catch(TwitterException t){
             System.out.println("TwitterException caught:" + t.getErrorMessage() + t.getExceptionCode());
                RateLimitStatus rate = t.getRateLimitStatus();
                if (rate == null) {
                    rate = twitter.getRateLimitStatus();
                }
                System.out.println("hourly limit:" + rate.getHourlyLimit());
                System.out.println("remaining hits:" + rate.getRemainingHits());
                System.out.println("reset time:" + rate.getResetTimeInSeconds());
                System.out.println("time until reset:" + rate.getSecondsUntilReset());
                if (rate.getRemainingHits() == 0) {
                    System.out.println("Sleeping start");
                    Thread.sleep(rate.getSecondsUntilReset() * 1001);
                    System.out.println("Sleeping end");
                    i--;
                    ids = null;
                }
        }
        try {
             if(twitter.getRateLimitStatus().getRemainingHits() == 350) continue;
            if (ids != null && twitter.getRateLimitStatus().getRemainingHits() != 350) {

                long result[] = ids.getIDs();
                File dir = new File("//home//shaleen//Twitter//Rajan2");
                File file = new File(dir, "followers_" + arr[i]);
                BufferedWriter outputofuser = new BufferedWriter(new FileWriter(file, false));
                for (int j = 0; j < result.length; j++) {

                    outputofuser.write(String.valueOf(result[j]));
                    outputofuser.write("\n");
                    outputofuser.flush();
                }

            }
        } catch (Exception e) {
            System.out.println("in for loop"+e.getMessage());




}

}






     }

private static void lookUpFriends(Twitter twitter, long[] arr) throws TwitterException, InterruptedException, IOException {


    for(int i = 0;i<arr.length;i++) {
 IDs ids = null;
        try{
        ids = twitter.getFriendsIDs(arr[i], -1);
        } catch(TwitterException t){
             System.out.println("TwitterException caught:" + t.getErrorMessage() + t.getExceptionCode());
                RateLimitStatus rate = t.getRateLimitStatus();
                if (rate == null) {
                    rate = twitter.getRateLimitStatus();
                }
                System.out.println("hourly limit:" + rate.getHourlyLimit());
                System.out.println("remaining hits:" + rate.getRemainingHits());
                System.out.println("reset time:" + rate.getResetTimeInSeconds());
                System.out.println("time until reset:" + rate.getSecondsUntilReset());
                if (rate.getRemainingHits() == 0) {
                    System.out.println("Sleeping start");
                    Thread.sleep(rate.getSecondsUntilReset() * 1001);
                    System.out.println("Sleeping end");
                    i--;
                    ids = null;
                }
        }
        try {

            if(twitter.getRateLimitStatus().getRemainingHits() == 350) continue;
            if (ids != null && twitter.getRateLimitStatus().getRemainingHits() != 350) {

                long result[] = ids.getIDs();
                File dir = new File("//home//shaleen//Twitter//Rajan2");
                File file = new File(dir, "friends_" + arr[i]);
                BufferedWriter outputofuser = new BufferedWriter(new FileWriter(file, false));
                for (int j = 0; j < result.length; j++) {

                    outputofuser.write(String.valueOf(result[j]));
                    outputofuser.write("\n");
                    outputofuser.flush();
                }

            }
        } catch (Exception e) {
            System.out.println("in for loop"+e.getMessage());




}
    }

    }


    private static void lookUpUsers(Twitter twitter, long[] arr) throws TwitterException, InterruptedException, IOException, JSONException {


        int slots = arr.length / 100;
        System.out.println("Total slots:" + slots);
        long idList[] = new long[100];
        int index = 0;
        int i;
        for ( i = 0; i <= slots; i++) {
        ResponseList<User> users = null;
    


            index = 0;
            for (int j = i * 100; j <= (i + 1) * 100 - 1 && j < arr.length; j++) {
                idList[index] = arr[j];
                System.out.println("idList index:" + index + " value:" + idList[index]);
                index++;
            }


        try{
        users = twitter.lookupUsers(idList);
        }catch(TwitterException t) {
             System.out.println("TwitterException caught:" + t.getErrorMessage() + t.getExceptionCode());
                RateLimitStatus rate = t.getRateLimitStatus();
                if (rate == null) {
                    rate = twitter.getRateLimitStatus();
                }
                System.out.println("hourly limit:" + rate.getHourlyLimit());
                System.out.println("remaining hits:" + rate.getRemainingHits());
                System.out.println("reset time:" + rate.getResetTimeInSeconds());
                System.out.println("time until reset:" + rate.getSecondsUntilReset());
                if (rate.getRemainingHits() == 0) {
                    System.out.println("Sleeping start");
                    Thread.sleep(rate.getSecondsUntilReset() * 1001);
                    System.out.println("Sleeping end");
                    i--;
                    users = null;
                }
        }


System.out.println(users.size());


                        if(users != null && twitter.getRateLimitStatus().getRemainingHits() != 350) {
                            for(int j = 0 ; j<users.size() ; j++) {
                               User user = users.get(j);
                                System.out.println(DataObjectFactory.getRawJSON(user));
                                File dir = new File("//home//shaleen//Twitter//Rajan2");
                                File file = new File(dir, "user_" + user.getId());
                                BufferedWriter outputofuser = new BufferedWriter(new FileWriter(file, false));
                               
                                JSONObject obj = new JSONObject(DataObjectFactory.getRawJSON(user));
                                outputofuser.write(obj.toString(5));
                                outputofuser.flush();
                               
                            }
                        }
        }

        
    }



}

