/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;

/*
 A helper class for UI testing. There is only one method within this class for the time being,
 which allows for testing of child views within a RecyclerView's ViewHolder. I wanted to create a
 Matcher myself purely because I wanted to play around with it.
 */
public class Utils {

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {

        // First we ensure that itemMatcher is not null. Otherwise, checkNotNull() will throw a
        // NullPointerException.
        checkNotNull(itemMatcher);

        // Then, we create a BoundedMatcher so that the matcher only matches children of a
        // RecyclerView.
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            // This method is only used to send debug output to the console
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            // This method is where we determine if the ViewHolder of the RecyclerView matches with
            // itemMatcher.
            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                // First we obtain a reference to the Viewholder and return false if it is null
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                // Otherwise, we attempt to match itemMatcher with the ViewHolder's itemView.
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

}
