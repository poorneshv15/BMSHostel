package com.psps.projects.bmshostel;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * A simple {@link Fragment} subclass.
 */


public  class StudentListFragment extends Fragment implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    HosteliteAdapter studentAdapter;
    MaterialSearchView materialSearchView;

    public StudentListFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }




    interface menuItemClick{
        void onMenuClick(int id);
    }
    private menuItemClick menuSelected;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.menuSelected=(menuItemClick)context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);//Make sure you have this line of code.
        View rootView=inflater.inflate(R.layout.fragment_student_list, container, false);
        studentAdapter=new HosteliteAdapter(rootView.getContext());
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(studentAdapter);

        materialSearchView=(MaterialSearchView)rootView.findViewById(R.id.search_view);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.hostel_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

//    protected abstract MenuInflater getLayoutInflater();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuSelected.onMenuClick(item.getItemId());
        return super.onOptionsItemSelected(item);
    }
}
