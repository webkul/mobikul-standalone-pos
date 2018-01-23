package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CategoryActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentAddCategoryBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;

public class AddCategoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "category";
    private static final String ARG_PARAM2 = "edit";

    private Category category;
    private boolean isEdit;
    public FragmentAddCategoryBinding binding;

    public AddCategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = (Category) getArguments().getSerializable(ARG_PARAM1);
            isEdit = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_category, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setData(category);
        if (isEdit) {
            ((CategoryActivity) getContext())
                    .setTitle(getContext().getString(R.string.edit_category_title));
            ((CategoryActivity) getActivity()).binding.delete.setVisibility(View.VISIBLE);
        } else {
            ((CategoryActivity) getContext())
                    .setTitle(getContext().getString(R.string.add_category_title));
        }
        ((CategoryActivity) getActivity()).binding.setData(category);
        ((CategoryActivity) getActivity()).binding.setIsEdit(isEdit);
        ((CategoryActivity) getActivity()).binding.addCategory.setVisibility(View.GONE);
        ((CategoryActivity) getActivity()).binding.save.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((CategoryActivity) getActivity()).binding.addCategory.setVisibility(View.VISIBLE);
        ((CategoryActivity) getActivity()).binding.save.setVisibility(View.GONE);
        ((CategoryActivity) getActivity()).binding.delete.setVisibility(View.GONE);
        getActivity().recreate();
        ((CategoryActivity) getContext())
                .setTitle(getContext().getString(R.string.title_activity_category));
    }
}
