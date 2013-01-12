/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.*;
/**
 *
 *
 *
 *
 *
 * @author shaleen
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import twitter4j.Status;

import twitter4j.Twitter;

import twitter4j.TwitterException;

import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.json.DataObjectFactory;

public class Main {

    /**

     * @param args

     */
    public static void main(String args[]) throws Exception {
        BufferedWriter out = new BufferedWriter(new FileWriter("output.txt", true));
        BufferedWriter arr = new BufferedWriter(new FileWriter("outputbfsarray.txt", true));


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

        AccessToken accessToken = new AccessToken("XXXXX","XXXXXX");//generate your own secret keys and replace in xxxxxx

        twitter.setOAuthAccessToken(accessToken);




        try {

            long cursor = -1;
            IDs ids = null;
            ArrayList<Long> bfsarray = new ArrayList<Long>();
            bfsarray.add(new Long(514071011));
            int genre = 0;

            while (genre <= 10) {
                cursor = -1;

                System.out.println("Listing followers's ids and generation" + bfsarray.get(0) + " " + genre);
                do {


                    //try catch needed here
                    try {
                        ids = twitter.getFollowersIDs(bfsarray.get(0), cursor);
                    } catch (TwitterException t) {

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
                        }
                    }

                    if (ids != null) {
                        System.out.println("IDs:" + ids.getIDs().length);
                        //if (ids.getNextCursor() == 0) {
                        bfsarray.remove(0);
                        //}
                        long list[] = new long[ids.getIDs().length];
                        list = ids.getIDs();
                        for (int i = 0; i < list.length; i++) {
                            System.out.println(list[i]);
                            arr.write(String.valueOf(list[i]));
                            arr.write("\n");
                            arr.flush();
                            bfsarray.add(new Long(list[i]));

                        }

                        System.out.println("bfsarray:" + bfsarray.toString());
                    }
                    if (ids != null && ids.getIDs().length > 0) {
                        getRawUserJson(ids, twitter, out);
                    }
                    RateLimitStatus rate = twitter.getRateLimitStatus();

                    System.out.println("hourly limit:" + rate.getHourlyLimit());
                    System.out.println("remaining hits:" + rate.getRemainingHits());
                    System.out.println("reset time:" + rate.getResetTimeInSeconds());
                    System.out.println("time until reset:" + rate.getSecondsUntilReset());
                    if (ids != null) {
                        //cursor = ids.getNextCursor();
                        cursor = 0;
                    }
                } while (cursor != 0);

                genre++;

            }


        } catch (TwitterException t) {
            System.out.println("Caught Exception");
            RateLimitStatus r = t.getRateLimitStatus();
            System.out.println(t.getErrorMessage() + t.getMessage() + t.getExceptionCode() + t.getStackTrace());
//    System.out.println("rate limit:"+r.getRemainingHits()+" "+r.getSecondsUntilReset());
        }



    }

    private static void getRawUserJson(IDs ids, Twitter twitter, BufferedWriter out) throws TwitterException, InterruptedException, IOException, JSONException {

        int slots = ids.getIDs().length / 100;
        System.out.println("Total slots:" + slots);
        long idList[] = new long[99];
        int index = 0;
        for (int i = 0; i <= slots; i++) {


            index = 0;
            for (int j = i * 100; j < (i + 1) * 100 - 1 && j < ids.getIDs().length; j++) {
                idList[index] = ids.getIDs()[j];
                System.out.println("idList index:" + index + " value:" + idList[index]);
                index++;
            }

            System.out.println("idList" + idList.length);
            System.out.println("slot:" + i);

            ResponseList<User> userInfo = null;
            //try caTCH NEEDED HERE
            try {
                userInfo = twitter.lookupUsers(idList);
            } catch (TwitterException t) {
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
                }
            }



            if (userInfo != null) {
                for (int ind = 0; ind < userInfo.size(); ind++) {
                    User u = userInfo.get(ind);
                    if (u != null) {
                        System.out.println(DataObjectFactory.getRawJSON(u));
                        JSONObject json = new JSONObject(DataObjectFactory.getRawJSON(u));
                        File dir = new File("//home//shaleen//" + u.getId());
                        dir.mkdirs();
                        File file = new File(dir, "user.txt");
                        BufferedWriter outputofuser = new BufferedWriter(new FileWriter(file, false));
                        outputofuser.write(json.toString(5));
                        outputofuser.flush();
                        System.out.println("Username id" + u.getName() + " " + u.getId());
                    }
                }
            }




            User user = null;
            if (userInfo != null) {
                for (int ind = 0; ind < userInfo.size(); ind++) {
                    user = userInfo.get(ind);
                    if (user != null) {
                        ResponseList<Status> status = null;
                        try {
                            System.out.println("User : " + user.getId());
                            status = twitter.getUserTimeline(user.getId());
                            System.out.println("Status : " + status.size());
                            if (status != null) {
                                System.out.println(DataObjectFactory.getRawJSON(status));
                                JSONArray json = new JSONArray(DataObjectFactory.getRawJSON(status));
                                File dir = new File("//home//shaleen//" + user.getId());
                                dir.mkdirs();
                                File file = new File(dir, "tweets.txt");
                                BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
                                output.write(json.toString(5));
                                output.flush();
                            }
                        } catch (TwitterException t) {
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
                            }
                        }
                    }
                }
            }













            RateLimitStatus rate = twitter.getRateLimitStatus();

            System.out.println("hourly limit:" + rate.getHourlyLimit());
            System.out.println("remaining hits:" + rate.getRemainingHits());
            System.out.println("reset time:" + rate.getResetTimeInSeconds());
            System.out.println("time until reset:" + rate.getSecondsUntilReset());
        }

    }
}




