package com.example.arek.baking;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.example.arek.baking.R;
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
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void recipeListHaveDataAndOpenCorrectRecipe() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_item_name_text_view), withText(RECIPE_NAME), isDisplayed()));
        textView.check(matches(withText(RECIPE_NAME)));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_list_recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        isCorrectToolbarTitleDisplayed();

        isIngredientsListDisplayed();

        isStepsRecipeListDisplayed();
    }

    private void isCorrectToolbarTitleDisplayed() {

        ViewInteraction textView3 = onView(allOf(
                isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))));
        textView3.check(matches(withText(RECIPE_NAME)));
    }

    private void isIngredientsListDisplayed() {
        ViewInteraction textView4 = onView(
                allOf(withId(R.id.item_ingredient_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipe_detail_ingredients_recycler_view),
                                        3),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("salt")));
    }

    private void isStepsRecipeListDisplayed() {

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.recipe_step_item_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipe_detail_recycler_view),
                                        3),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Press the crust into baking form.")));
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
