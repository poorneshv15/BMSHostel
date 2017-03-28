package com.psps.projects.bmshostel;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteHosteliteFragment extends Fragment implements SearchView.OnQueryTextListener,LoaderManager.LoaderCallbacks<Cursor>{

    RecyclerView recyclerView;
    DeleteHosteliteAdapter studentAdapter;
    List<Student> studentList;
    MaterialSearchView materialSearchView;

    public DeleteHosteliteFragment() {
        // Required empty public constructor
    }
    interface menuItemClickOfDHS {
        void onMenuOfDHFClick(int id);
    }
    private menuItemClickOfDHS menuSelected;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.menuSelected=(menuItemClickOfDHS)context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_student_list, container, false);
        studentList=new ArrayList<>();
        studentList.add(new Student("Poornesh","CSE",6,2));
        studentList.add(new Student("Pratik","CSE",4,32));
        studentList.add(new Student("Shashikant","CSE",3,2));
        studentList.add(new Student("Kudva","BIO",4,3));
        studentAdapter=new DeleteHosteliteAdapter(studentList,rootView.getContext());
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(studentAdapter);
        materialSearchView=(MaterialSearchView)rootView.findViewById(R.id.search_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuSelected.onMenuOfDHFClick(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
