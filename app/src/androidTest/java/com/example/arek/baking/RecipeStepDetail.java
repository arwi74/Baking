package com.example.arek.baking;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.arek.baking.recipeList.RecipeListActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeStepDetail {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);
    private IdlingResource mIdlingResource;
    private boolean mIsTwoPane;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        mIsTwoPane = mActivityTestRule.getActivity().isTwoPane();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void recipeStepDetail() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_list_recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        if ( mIsTwoPane ) {
            isFirstStepDisplayedInTwoPaneMode();
        }

        isCorrectStepOpenOnClick();

    }

    private void isCorrectStepOpenOnClick() {
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recipe_detail_recycler_view),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.recipe_step_short_description),
                        isDisplayed()));
        textView2.check(matches(withText("Add butter and milk to dry ingredients.")));
    }

    private void isFirstStepDisplayedInTwoPaneMode() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_step_short_description),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_detail_recipe_step),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Recipe Introduction")));
    }




    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @After
    public void unregisterIdlingResource() {
        if ( mIdlingResource != null ) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
