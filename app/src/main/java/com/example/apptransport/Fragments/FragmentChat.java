package com.example.apptransport.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Adapter.ChatMessageAdapter;
import com.apreciasoft.mobile.asremis.Entity.Chat;
import com.apreciasoft.mobile.asremis.Entity.ChatMessageReceive;
import com.apreciasoft.mobile.asremis.Entity.ChatMessageReceiveSaved;
import com.apreciasoft.mobile.asremis.Entity.ChatOperator;
import com.apreciasoft.mobile.asremis.Entity.ChatOperatorReceive;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.apreciasoft.mobile.asremis.viewmodels.ChatViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
public class FragmentChat extends BaseFragment {
    public static boolean isChatFragmentVisible=false;
    private View myView;
    ChatMessageAdapter messageAdapter;
    List<Chat> listChat;
    RecyclerView recyclerViewChat;
    ImageButton btnSendMessage;
    EditText editTextMessage;
    ChatViewModel chatViewModel;
    public ServicesTravel daoTravel = null;
    public static GlovalVar gloval;
    private String idOperator;

    public FragmentChat(String idOperator){
        this.idOperator=idOperator;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_chat, container, false);
        gloval = ((GlovalVar) getActivity().getApplicationContext());
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeChatViewModel();
        initializeObserverChatViewModel();
        loadMessagedOld();
        listChat=new ArrayList<>();
        messageAdapter=new ChatMessageAdapter(getContext(),listChat,"1");
        recyclerViewChat = view.findViewById(R.id.recycler_view_chat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        recyclerViewChat.setAdapter(messageAdapter);

        editTextMessage = view.findViewById(R.id.txtMessage);

        btnSendMessage = view.findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=editTextMessage.getText().toString();
                if(!TextUtils.isEmpty(message)){
                    listChat.add(new Chat("1","2",message, Utils.getDDMMYYYY_hhmmss(new Date()) ));
                    messageAdapter.notifyDataSetChanged();
                    editTextMessage.setText("");
                    recyclerViewChat.scrollToPosition(listChat.size() - 1);
                    Utils.sendChatMessageToService(getActivity(),
                            message,
                            idOperator);
                    //recyclerViewChat.scrollToPosition();
                    //checkIfMessageWasSentIn2Seconds(message);
                }
            }
        });
    }

    @Override
    public void onResume() {
        isChatFragmentVisible=true;
        hideStreetTravelButtonInActivity();
        super.onResume();
    }

    @Override
    public void onPause() {
        isChatFragmentVisible=false;
        super.onPause();
    }

    @Override
    public boolean onBackPressed() {
        try{
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack ();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return true;
    }

    private void clearChatTemp(){
        try{
            if(chatViewModel!=null){
                chatViewModel.clearChat();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initializeChatViewModel()
    {
        try{
            chatViewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
            chatViewModel.clearChat();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void initializeObserverChatViewModel(){
        LiveData<Chat> liveDataChat = chatViewModel.listChats();
        liveDataChat.observe(getViewLifecycleOwner(), new Observer<Chat>() {
            @Override
            public void onChanged(Chat chatItem) {
                if(chatItem!=null){
                    Log.e("CHAT", chatItem.getMessage());
                    listChat.add(chatItem);
                    messageAdapter.notifyDataSetChanged();
                    recyclerViewChat.scrollToPosition(listChat.size() - 1);
                    chatViewModel.clearChat();
                }
            }
        });
    }

    private void loadMessagedOld(){
        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
        Call<ChatMessageReceiveSaved> call;
        int cantMessages=20;
        call = daoTravel.listChatMessages(String.valueOf(gloval.getGv_id_driver()), String.valueOf(cantMessages), idOperator);
        call.enqueue(new Callback<ChatMessageReceiveSaved>() {
            @Override
            public void onResponse(Call<ChatMessageReceiveSaved> call, Response<ChatMessageReceiveSaved> response) {
                if(response.isSuccessful())
                {
                    try{
                        ChatMessageReceiveSaved result = response.body();
                        List<Chat> listChatReceived = getListChatFromReceived(result.listMessagesSaved);

                        listChat.addAll(listChatReceived);
                        messageAdapter.notifyDataSetChanged();
                        recyclerViewChat.scrollToPosition(listChat.size() - 1);
                        //setLastChatRoomUsedFromMessagesSaved(listChatReceived);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ChatMessageReceiveSaved> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadChatOperators(){
        /*if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
        Call<List<ChatOperator>> call;
        String idOperador="2";
        int cantMessages=50;
        call = daoTravel.listChatOperators();
        call.enqueue(new Callback<ChatOperatorReceive>() {
            @Override
            public void onResponse(Call<ChatOperatorReceive> call, Response<ChatOperatorReceive> response) {
                if(response.isSuccessful())
                {
                    try{
                        ChatOperatorReceive result = response.body();
                        List<ChatOperator> listChatReceived = result.getOperators();
                        Log.e("CHAT", listChatReceived.toString());
                        //listChat.addAll(listChatReceived);
                        //messageAdapter.notifyDataSetChanged();
                        //recyclerViewChat.scrollToPosition(listChat.size() - 1);
                        //setLastChatRoomUsedFromMessagesSaved(listChatReceived);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ChatOperatorReceive> call, Throwable t) {
                t.printStackTrace();
            }
        });*/
    }

    private void setLastChatRoomUsedFromMessagesSaved(List<Chat> listChats){

        chatViewModel.setLastChatMessageReceived(listChats.get(listChats.size()-1));
    }

    private void hideStreetTravelButtonInActivity(){
        try{
            HomeChofer.MUST_SHOW_STREET_BUTTON=false;
            ((HomeChofer) Objects.requireNonNull(getActivity())).showOrHideStreetButton(false);
            Log.e("CHAT", "Se ocultó el botón");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private List<Chat> getListChatFromReceived(List<ChatMessageReceive> listMessages){
        List<Chat> listResult=new ArrayList<>();
        //Collections.reverse(listMessages);
        for (ChatMessageReceive item : listMessages) {
            listResult.add(new Chat(item));
        }
        return listResult;
    }

    private void checkIfMessageWasSentIn2Seconds(String messageSent){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfMessageWasSent(messageSent);
            }
        }, 2000);
    }

    private void checkIfMessageWasSent(String messageSent) {
        try {
            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }
            Call<ChatMessageReceiveSaved> call;
            int cantMessages = 3;
            call = daoTravel.listChatMessages(String.valueOf(gloval.getGv_id_driver()), String.valueOf(cantMessages), idOperator);
            call.enqueue(new Callback<ChatMessageReceiveSaved>() {
                @Override
                public void onResponse(Call<ChatMessageReceiveSaved> call, Response<ChatMessageReceiveSaved> response) {
                    try {
                        if (response.isSuccessful()) {
                            ChatMessageReceiveSaved result = response.body();
                            boolean found=false;
                            for (ChatMessageReceive item : result.listMessagesSaved) {
                                if ("0".equals(item.getChatFromWeb()) && item.getChatMessage().equals(messageSent)) {
                                    found=true;
                                    break;
                                }
                            }
                            if(!found){
                                pintarMensajeEnviado(messageSent);
                            }
                            messageAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ChatMessageReceiveSaved> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pintarMensajeEnviado(String messageSent) {
        for(int i=listChat.size()-1;i>=0;i++) {
            if(listChat.get(i).getMessage().equals(messageSent)){
                listChat.get(i).setWasSent(false);
                break;
            }
        }
    }


}
