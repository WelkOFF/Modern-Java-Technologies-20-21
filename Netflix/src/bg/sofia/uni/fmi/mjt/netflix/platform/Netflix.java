package bg.sofia.uni.fmi.mjt.netflix.platform;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;

public class Netflix implements StreamingService {

    public Netflix(Account[] accounts, Streamable[] streamableContent) {
        this.accounts = accounts;
        this.streamableContent = streamableContent;
    }

    /**
     * Simulates watching activity for the given user.
     *
     * @param user             the user that will watch the video. The user must be registered in the platform in order to access its contents.
     * @param videoContentName the exact name of the video content: movie or series
     *                         If the content is of type Series, we assume that the user will watch all episodes in it.
     * @throws ContentUnavailableException if the content is age restricted and the user is not yet permitted to access it.
     * @throws UserNotFoundException       if the user is not registered in the platform.
     * @throws ContentNotFoundException    if the content is not present in the platform.
     */
    @Override
    public void watch(Account user, String videoContentName)
            throws ContentUnavailableException {
        if(userIsRegistered(user)) {
            if (contentExists(videoContentName)) {

                Streamable currentContent = getContentByName(videoContentName);
                if (isAgeRatingCompatible(user, currentContent)) {
                    ((AbstractContent)currentContent).increaseViewCount();
                }
                else
                {
                    throw new ContentUnavailableException();
                }
            }
            else
            {
                throw new ContentNotFoundException();
            }
        }
        else
        {
            throw new UserNotFoundException();
        }


    }

    /**
     * @param videoContentName the exact name of the video content: movie or series
     * @return the Streamable resource with name that matches the provided name or null if no such content exists in the platform.
     */
    @Override
    public Streamable findByName(String videoContentName) {
        for(Streamable streamableVideoContent : streamableContent)
        {
            if(videoContentName.equals(streamableVideoContent.getTitle()))return  streamableVideoContent;
        }
        return null;
    }

    /**
     * @return the most watched Streamable resource available in the platform or null if no streams were done yet.
     */
    @Override
    public Streamable mostViewed() {
        int mostViews = 0;
        Streamable mostViewedResource = null;
        for(Streamable content : streamableContent)
        {
            if(mostViews < ((AbstractContent)content).getViews())
            {
                mostViews = ((AbstractContent) content).getViews();
                mostViewedResource = content;
            }
        }
        return mostViewedResource;
    }

    /**
     * @return the minutes spent by all users registered in the platform while watching streamable content.
     */
    @Override
    public int totalWatchedTimeByUsers() {

        int totalWatchedTime = 0;
        for(Streamable content : streamableContent)
        {
            totalWatchedTime+= ((AbstractContent) content).getViews()*content.getDuration();
        }
        return  totalWatchedTime;
    }

    private boolean userIsRegistered(Account account)
    {
        for(Account registeredUser : accounts )
        {
            if(registeredUser.equals(account))return true;
        }
        return false;
    }

    private boolean contentExists(String contentTitle)
    {
        for(Streamable existingContent : streamableContent )
        {
            if(existingContent.getTitle().equals(contentTitle))return true;
        }
        return false;
    }

    private Streamable getContentByName(String contentTitle)
    {
        for(Streamable existingContent : streamableContent )
        {
            if(existingContent.getTitle().equals(contentTitle))return existingContent;
        }
        return null;
    }

    private boolean isAgeRatingCompatible(Account account,Streamable content)
    {
        switch (content.getRating())
        {
            case G: return true;
            case PG13: return account.getAge()>=13;
            case NC17: return account.getAge()>=17;
        }
        return false;
    }


    private Account[] accounts;
    private Streamable[] streamableContent;
    
}
