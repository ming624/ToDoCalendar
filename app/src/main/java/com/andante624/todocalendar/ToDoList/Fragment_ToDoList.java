package com.andante624.todocalendar.ToDoList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.andante624.todocalendar.DataManage.DB.DBManager;
import com.andante624.todocalendar.R;


public class Fragment_ToDoList extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_parent, spinner_child;
    private ListView todo_listview;
    private ToDo_ListViewAdapter toDo_listViewAdapter;
    private SpinnerAdapter_ToDo_Parent spinnerAdapter_todo_parent;
    private SpinnerAdapter_ToDo_Child spinnerAdapter_todo_child;
    private OnFragmentInteractionListener mListener;

    public static Fragment_ToDoList newInstance(String param1, String param2) {
        Fragment_ToDoList fragment = new Fragment_ToDoList();

        return fragment;
    }
    public Fragment_ToDoList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_default,menu);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void init()
    {
        toDo_listViewAdapter = new ToDo_ListViewAdapter();
        toDo_listViewAdapter.setFragment(this);
        toDo_listViewAdapter.setContext(getActivity());

        spinnerAdapter_todo_parent = new SpinnerAdapter_ToDo_Parent();
        spinnerAdapter_todo_parent.setContext(getActivity().getApplicationContext());
        spinnerAdapter_todo_parent.setItems();

        spinnerAdapter_todo_child = new SpinnerAdapter_ToDo_Child();
        spinnerAdapter_todo_child.setContext(getActivity().getApplicationContext());
        spinnerAdapter_todo_child.setItems();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        setLayout(view);
        spinnerAdapter_todo_child.updateCategory();

        return view;
    }

    public void setLayout(View view)
    {
        todo_listview = (ListView)view.findViewById(R.id.todolist_todolist);
        todo_listview.setAdapter(toDo_listViewAdapter);

        toDo_listViewAdapter.setListview(todo_listview);

        spinner_parent = (Spinner)view.findViewById(R.id.todolist_parents_spinner);
        spinner_parent.setAdapter(spinnerAdapter_todo_parent);
        spinner_parent.setOnItemSelectedListener(this);


        spinner_child = (Spinner)view.findViewById(R.id.todolist_child_spinner);
        spinner_child.setAdapter(spinnerAdapter_todo_child);
        spinner_child.setOnItemSelectedListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.todolist_parents_spinner:
                switch (position) {
                    case 0:
                        spinner_child.setVisibility(View.INVISIBLE);
                        requestToDoItems(DBManager.WHERE_COMPARISON_7DEADLINE_DATE,null);
                        break;
                    case 1:
                        spinner_child.setVisibility(View.INVISIBLE);
                        requestToDoItems(DBManager.WHERE_COMPARISON_7CREATE_DATE,null);
                        break;
                    case 2:
                        spinnerAdapter_todo_child.setSelect(1);
                        spinner_child.setVisibility(View.VISIBLE);
                        spinner_child.setSelection(0);
                        spinner_child.getOnItemSelectedListener().onItemSelected(spinner_child,spinner_child.getSelectedView(),0,0);
                        spinnerAdapter_todo_child.notifyDataSetChanged();
                        break;
                    case 3:
                        spinnerAdapter_todo_child.setSelect(0);
                        spinner_child.setVisibility(View.VISIBLE);
                        spinner_child.setSelection(0);
                        spinner_child.getOnItemSelectedListener().onItemSelected(spinner_child, spinner_child.getSelectedView(), 0, 0);
                        spinnerAdapter_todo_child.notifyDataSetChanged();
                        break;
                    case 4:
                        spinner_child.setVisibility(View.INVISIBLE);
                        requestToDoItems(DBManager.WHERE_COMPARISON_DEADLINE_DATE,null);
                        break;
                    case 5:
                        spinner_child.setVisibility(View.INVISIBLE);
                        requestToDoItems(DBManager.WHERE_COMPARISON_COMPLETE_DATE, null);
                        break;
                }
                break;
            case R.id.todolist_child_spinner:
                if(spinner_child.getVisibility() == View.VISIBLE) {
                    if (spinnerAdapter_todo_child.getSelect() == 1) //category
                    {
                        String category_title = spinnerAdapter_todo_child.getItem(position);
                        requestToDoItems(DBManager.WHERE_MATCH_CATEGORY, category_title);
                    } else {
                        requestToDoItems(DBManager.WHERE_MATCH_IMPORTANCE, position + "");
                    }
                }
                break;
        }
    }

    public void requestToDoItems(int where, String condition)
    {
        DBManager dbManager = DBManager.open(getActivity().getApplicationContext());
        toDo_listViewAdapter.setTodolist_items(dbManager.select_ToDoItems(where, condition));
        dbManager.close();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
