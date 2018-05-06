package com.example.arek.baking.recipeDetails;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arek.baking.BakingApp;
import com.example.arek.baking.R;
import com.example.arek.baking.adapter.IngredientsAdapter;
import com.example.arek.baking.adapter.RecipeStepAdapter;
import com.example.arek.baking.databinding.FragmentRecipeDetailBinding;
import com.example.arek.baking.model.Ingredient;
import com.example.arek.baking.model.Step;
import com.example.arek.baking.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailActivityFragment extends Fragment implements RecipeDetailContract.View{
    private FragmentRecipeDetailBinding mBinding;
    private long mRecipeId;
    private int mSelectedItem;
    private RecipeStepAdapter mAdapter;
    private IngredientsAdapter mIgredientsAdapter;
    private RecipeDetailContract.Presenter mPresenter;
    @Inject
    public RecipeRepository mRepository;
    private RecipeDetailActivityListener mListener;
  //  private static final String STATE_RECIPE_ID = "state_recipe_id";
    private static final String STATE_SELECTED_ITEM = "state_selected_item";

    public RecipeDetailActivityFragment() {}

    public static RecipeDetailActivityFragment newInstance(long recipeId) {
        RecipeDetailActivityFragment fragment = new RecipeDetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RecipeDetailActivity.EXTRA_RECIPE_ID,recipeId);
        Timber.d("new instance, recipeId: " +recipeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface RecipeDetailActivityListener {
        void onRecipeStepClick(long recipeStepId);
        void onTitleSet(String title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if ( arguments != null && arguments.containsKey(RecipeDetailActivity.EXTRA_RECIPE_ID) ) {
            mRecipeId = arguments.getLong(RecipeDetailActivity.EXTRA_RECIPE_ID);
        }
        if ( savedInstanceState != null && savedInstanceState.containsKey(STATE_SELECTED_ITEM) ) {
            mSelectedItem = savedInstanceState.getInt(STATE_SELECTED_ITEM);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BakingApp)getActivity().getApplication()).getRepositoryComponent().inject(this);
        mPresenter = new RecipeDetailPresenter(mRepository);
        mPresenter.takeView(this);
        setRecyclerView();
        setIngredientsRecyclerView();
        Timber.d("Recipe id" + mRecipeId);
        mPresenter.start(mRecipeId);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mSelectedItem = mAdapter.getSelectedItemPosition();
        outState.putInt(STATE_SELECTED_ITEM, mSelectedItem);
    }

    private void setIngredientsRecyclerView() {
        RecyclerView recycler = mBinding.recipeDetailIngredientsRecyclerView;
        RecyclerView.LayoutManager layout = new LinearLayoutManager(
                getActivity(),LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layout);
        mIgredientsAdapter = new IngredientsAdapter();
        recycler.setAdapter(mIgredientsAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if ( getActivity() instanceof RecipeDetailActivityListener ){
            mListener = (RecipeDetailActivityListener)getActivity();
        }else {
            throw new IllegalArgumentException("Activity must implement RecipeDetailActivityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setRecyclerView() {
        RecyclerView recycler = mBinding.recipeDetailRecyclerView;
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layout);
        mAdapter = new RecipeStepAdapter((RecipeStepAdapter.RecipeStepListener)mPresenter, getActivity());
        recycler.setAdapter(mAdapter);

    }

    @Override
    public void showRecipeSteps(List<Step> steps) {
        mAdapter.swapRecipeStep(steps);
        mAdapter.setSelectedItemPosition(mSelectedItem);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        mIgredientsAdapter.swapData(ingredients);
    }

    @Override
    public void showProgressBar() {
        mBinding.recipeDetailProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mBinding.recipeDetailProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void openRecipeStep(int recipeStepId) {
        mListener.onRecipeStepClick(recipeStepId);
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void showTitle(String title) {
        if ( mListener != null ) {
            mListener.onTitleSet(title);
        }
    }
}
