package com.example.arek.baking.recipeDetails;

import android.app.Application;
import android.databinding.DataBindingUtil;
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
import com.example.arek.baking.adapter.RecipeStepAdapter;
import com.example.arek.baking.api.BakingApi;
import com.example.arek.baking.databinding.FragmentRecipeDetailBinding;
import com.example.arek.baking.model.Step;
import com.example.arek.baking.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailActivityFragment extends Fragment implements RecipeDetailContract.View{
    private FragmentRecipeDetailBinding mBinding;
    private long mRecipeId;
    private RecipeStepAdapter mAdapter;
    private RecipeDetailContract.Presenter mPresenter;
    @Inject
    public RecipeRepository mRepository;

    public RecipeDetailActivityFragment() {

    }

    public static RecipeDetailActivityFragment newInstance(long recipeId) {
        RecipeDetailActivityFragment fragment = new RecipeDetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RecipeDetailActivity.EXTRA_RECIPE_ID,recipeId);
        fragment.setArguments(bundle);
        return fragment;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BakingApp)getActivity().getApplication()).getRepositoryComponent().inject(this);
        mPresenter = new RecipeDetailPresenter(mRepository);
        mPresenter.takeView(this);
        setRecyclerView();
        mPresenter.start(mRecipeId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    private void setRecyclerView() {
        RecyclerView recycler = mBinding.recipeDetailRecyclerView;
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layout);
        mAdapter = new RecipeStepAdapter((RecipeStepAdapter.RecipeStepListener)mPresenter);
        recycler.setAdapter(mAdapter);
    }

    @Override
    public void showRecipeSteps(List<Step> steps) {
        mAdapter.swapRecipeStep(steps);
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

    }

    @Override
    public void showErrorMessage() {

    }
}
