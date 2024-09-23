package com.example.apptransport.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Adapter.ChatOperatorsRecyclerViewAdapter;
import com.apreciasoft.mobile.asremis.Entity.ChatOperator;
import com.apreciasoft.mobile.asremis.Entity.ChatOperatorReceive;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Fragments.placeholder.PlaceholderContent;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.mercadopago.android.px.internal.callbacks.RecyclerItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class FragmentChatOperators extends BaseFragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public ServicesTravel daoTravel = null;

    private List<ChatOperator> listChatOperators;
    private ChatOperatorsRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentChatOperators() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentChatOperators newInstance(int columnCount) {
        FragmentChatOperators fragment = new FragmentChatOperators();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_operators_list, container, false);

        // Set the adapter
        listChatOperators=new ArrayList<>();
        adapter=new ChatOperatorsRecyclerViewAdapter(getContext(), listChatOperators);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view,final int position) {
                    openChatFragment(listChatOperators.get(position));
                }
            }));
        }
        return view;
    }

    private void openChatFragment(ChatOperator chatOperator) {
        try{
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new FragmentChat(chatOperator.getIdUser())).addToBackStack("chat").commitAllowingStateLoss();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadChatOperators();
    }

    @Override
    public boolean onBackPressed() {
        try{
            FragmentManager fr = getActivity().getSupportFragmentManager();
            if(getActivity() instanceof HomeChofer){
                HomeChofer activityChofer = (HomeChofer) getActivity();
                fr.beginTransaction().replace(R.id.content_frame, new HomeFragment(activityChofer)).commitAllowingStateLoss();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return true;
    }

    private void loadChatOperators(){
        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
        Call<List<ChatOperator>> call;
        call = daoTravel.listChatOperators();
        call.enqueue(new Callback<List<ChatOperator>>() {
            @Override
            public void onResponse(Call<List<ChatOperator>> call, Response<List<ChatOperator>> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    try {
                        listChatOperators.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ChatOperator>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}