package com.f22labs.instalikefragmenttransaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.InstantSearchHelper;
import com.algolia.instantsearch.ui.utils.ItemClickSupport;
import com.algolia.instantsearch.ui.views.Hits;
import com.algolia.instantsearch.ui.views.SearchBox;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.firebase.ui.auth.ui.User;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchFragment extends BaseFragment{

    private static final String ALGOLIA_APP_ID="DSCSZ3T7RV";
    private static final String ALGOLIA_SEARCH_API_KEY="f053a289481ef3d05a1737c503ccca24";
    private static final String ALGOLIA_INDEX_NAME="Instagram";

    private SearchBox searchBox;
    private Hits hits;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ((MainActivity)getActivity()).search();


        hits = (Hits) view.findViewById(R.id.hits);
        searchBox = (SearchBox) view.findViewById(R.id.search_edit_frame2);

        Searcher searcher = new Searcher(ALGOLIA_APP_ID,ALGOLIA_SEARCH_API_KEY,ALGOLIA_INDEX_NAME);
        InstantSearchHelper helper = new InstantSearchHelper(hits,searcher);
        helper.registerSearchView(getActivity(),searchBox);
        helper.search();
        hits.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, int position, View v) {
               JSONObject object = hits.get(position);
                try {
                    String id = object.getString("objectID");
                    if(id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        ((MainActivity)getActivity()).profil_tiklandi();
                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",id);

                        User_Profil profil = new User_Profil();
                        profil.setArguments(bundle);

                        mFragmentNavigation.pushFragment(profil);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        view.setOnTouchListener(new View.OnTouchListener() {  /////BOŞ YERE TIKLANDIĞINDA KALVYE KAPAT
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getView().getWindowToken(),0);
                return false;
            }
        });

        return view;
    }


}
